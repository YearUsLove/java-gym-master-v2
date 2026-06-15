package ru.yandex.practicum.gym.timetable;

import ru.yandex.practicum.gym.model.*;
import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }

        daySchedule.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> result = new ArrayList<>();

        for (TimeOfDay time : daySchedule.navigableKeySet()) {
            result.addAll(daySchedule.get(time));
        }

        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        return sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCounts.merge(coach, 1, Integer::sum);
                }
            }
        }

        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(coachCounts.entrySet());
        entries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        Map<Coach, Integer> sortedResult = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : entries) {
            sortedResult.put(entry.getKey(), entry.getValue());
        }

        return sortedResult;
    }
}