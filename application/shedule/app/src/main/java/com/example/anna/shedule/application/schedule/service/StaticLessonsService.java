package com.example.anna.shedule.application.schedule.service;

import com.example.anna.shedule.application.database.Database;
import com.example.anna.shedule.application.schedule.model.StaticLesson;
import com.example.anna.shedule.application.schedule.model.WeekPeriodicity;
import com.example.anna.shedule.application.user.service.RequestFactory;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;

import java.util.List;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class StaticLessonsService {

    private GroupService groupService;
    private RequestFactory requests;

    public StaticLessonsService() {
        this.groupService = new GroupService();
        this.requests = new RequestFactory();
    }

    public List<StaticLesson> getLessons(WeekPeriodicity periodicity, int dayOfWeek) {
        String query = "SELECT * FROM " + StaticLesson.TABLE_NAME
                + " WHERE dayOfWeek='" + dayOfWeek
                + "' AND " + getPeriodicityCondition(periodicity);

        return getLessonsByQuery(query);
    }

    private String getPeriodicityCondition(WeekPeriodicity periodicity) {
        return "(weekPeriodicity='" + periodicity.getId() + "' OR weekPeriodicity='" + WeekPeriodicity.BOTH.getId() + "')";
    }

    private List<StaticLesson> getLessonsByQuery(String query) {
        List<StaticLesson> lessons = getDbInstance().getByQuery(StaticLesson.class, query);
        groupService.mapGroupsOnLessons(lessons);
        return lessons;
    }

    public boolean updateLessons() {
        ServerResponseArray<StaticLesson> response = requests.getScheduleByCurrentUser();
        boolean isSuccess = response.isSuccess();
        if (isSuccess) {
            updateLessonsInDb(response.getResponse());
        }
        return isSuccess;
    }

    private void updateLessonsInDb(List<StaticLesson> lessons) {
        Database db = getDbInstance();
        db.dropAllElements(StaticLesson.TABLE_NAME);
        db.save(lessons);
    }

    public List<StaticLesson> getAllLessons() {
        String query = "SELECT * FROM " + StaticLesson.TABLE_NAME;
        return getLessonsByQuery(query);
    }
}