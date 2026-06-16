package ru.yandex.practicum.gym.timetable;

import ru.yandex.practicum.gym.model.*;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;
    private final Map<DayOfWeek, List<TrainingSession>> sessionsByDay;

    public Timetable() {
        timetable = new EnumMap<>(DayOfWeek.class);
        sessionsByDay = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
            sessionsByDay.put(day, new ArrayList<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        daySchedule.computeIfAbsent(time, key -> new ArrayList<>()).add(trainingSession);

        List<TrainingSession> daySessions = sessionsByDay.get(day);
        int insertIndex = findInsertIndex(daySessions, trainingSession);
        daySessions.add(insertIndex, trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return Collections.unmodifiableList(sessionsByDay.get(dayOfWeek));
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        if (sessions == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(sessions);
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (List<TrainingSession> daySessions : sessionsByDay.values()) {
            for (TrainingSession session : daySessions) {
                coachCounts.merge(session.getCoach(), 1, Integer::sum);
            }
        }

        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(coachCounts.entrySet());
        entries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Map<Coach, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private int findInsertIndex(List<TrainingSession> daySessions, TrainingSession newSession) {
        int left = 0;
        int right = daySessions.size();

        while (left < right) {
            int middle = (left + right) / 2;
            TrainingSession middleSession = daySessions.get(middle);

            if (middleSession.getTimeOfDay().compareTo(newSession.getTimeOfDay()) <= 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }
}