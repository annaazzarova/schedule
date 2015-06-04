package com.example.anna.shedule.application.schedule.service;

import com.example.anna.shedule.application.schedule.model.Group;
import com.example.anna.shedule.application.schedule.model.StaticLesson;
import com.example.anna.shedule.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class GroupService {

    private List<Group> groups;

    public List<StaticLesson> mapGroupsOnLessons(List<StaticLesson> lessons) {
        Set<String> setOfGroupIds = new HashSet<>();
        Map<Long, List<String>> lessonIdToGroupIds = new HashMap<>();
        Map<String, Group> groupIdToGroup = new HashMap<>();

        for (StaticLesson lesson: lessons) {
            String groupIds = lesson.getGroupIds();
            if (groupIds != null) {
                List<String> ids = StringUtils.split(groupIds, ',');
                lessonIdToGroupIds.put(lesson.getId(), ids);
                setOfGroupIds.addAll(ids);
            }
        }

        List<Group> groups = getGroupsByIds(setOfGroupIds);
        for (Group group: groups) {
            if (group.getGroupId() != null) {
                groupIdToGroup.put(group.getGroupId(), group);
            }
        }

        for (StaticLesson lesson: lessons) {
            List<String> groupIds = lessonIdToGroupIds.get(lesson.getId());
            mapGroupsOnLesson(lesson, groupIds, groupIdToGroup);
        }

        return lessons;
    }

    public Set<String> getFaculties() {
        Set<String> faculties = new HashSet<>();
        for (Group group: getGroups()) {
            String faculty = group.getFaculty();
            if (faculty != null) {
                faculties.add(faculty);
            }
        }
        return faculties;
    }

    public Set<String> getSpecialities(String faculty) {
        Set<String> specialities = new HashSet<>();
        for (Group group: getGroups()) {
            String groupFaculty = group.getFaculty();
            String speciality = group.getSpecialty();
            if (faculty.equals(groupFaculty) && speciality != null) {
                specialities.add(speciality);
            }
        }
        return specialities;
    }

    public Set<String> getCourses(String faculty, String speciality) {
        Set<String> courses = new HashSet<>();
        for (Group group: getGroups()) {
            String groupFaculty = group.getFaculty();
            String groupSpecialty = group.getSpecialty();
            String course = group.getCourse();
            if (faculty.equals(groupFaculty) && speciality.equals(groupSpecialty) && course != null) {
                courses.add(course);
            }
        }
        return courses;
    }

    public List<Group> getGroups(String faculty, String speciality, String course) {
        List<Group> groups = new ArrayList<>();
        for (Group group: getGroups()) {
            String groupFaculty = group.getFaculty();
            String groupSpecialty = group.getSpecialty();
            String groupCourse = group.getCourse();
            if (faculty.equals(groupFaculty) && speciality.equals(groupSpecialty) && course.equals(groupCourse)) {
                groups.add(group);
            }
        }
        return groups;
    }

    private void mapGroupsOnLesson(StaticLesson lesson, List<String> groupIds, Map<String, Group> groupIdToGroup) {
        if (groupIds == null) return;
        List<Group> groups = new ArrayList<>(groupIds.size());
        for (String groupId: groupIds) {
            Group group = groupIdToGroup.get(groupId);
            if (group != null) {
                groups.add(group);
            }
        }
        lesson.setGroups(groups);
    }

    public List<Group> getGroupsByIds(Collection<String> groupIds) {
        if (groupIds.isEmpty()) return Collections.emptyList();

        String groupIdsAsString = StringUtils.join(groupIds, "', '");
        String query = "SELECT * FROM " + Group.TABLE_NAME + " WHERE groupId in ('" + groupIdsAsString + "')";
        return getDbInstance().getByQuery(Group.class, query);
    }

    public List<Group> getGroups() {
        if (groups == null) {
            groups = getGroupsFromDb();
        }
        return groups;
    }

    public List<Group> getGroupsFromDb() {
        return getDbInstance().getAll(Group.class);
    }
}