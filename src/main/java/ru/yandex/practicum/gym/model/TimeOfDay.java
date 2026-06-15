package ru.yandex.practicum.gym.model;

public class TimeOfDay implements Comparable<TimeOfDay> {

    private int hours;
    private int minutes;

    public TimeOfDay(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public int compareTo(TimeOfDay other) {
        int thisTotalMinutes = this.hours * 60 + this.minutes;
        int otherTotalMinutes = other.hours * 60 + other.minutes;
        return Integer.compare(thisTotalMinutes, otherTotalMinutes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeOfDay timeOfDay = (TimeOfDay) obj;
        return hours == timeOfDay.hours && minutes == timeOfDay.minutes;
    }

    @Override
    public int hashCode() {
        return 31 * hours + minutes;
    }
}