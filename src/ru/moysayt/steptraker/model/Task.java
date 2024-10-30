package ru.moysayt.steptraker.model;

import java.util.Objects;

public class Task {
    private int id; // Непонятно почему приват, а не финал
    private String title;
    private String text;
    private  StatusOfTask status;

    /*private закрывает доступ к данным из вне, т.е. делает поля класса доступными только внутри самого класса.
    Получать доступ к полям и менять значения можно через геттеры/сеттеры.
    final это ключевое слово, которое указывает, что значение объекта не должно меняться.*/

//    Так в том то и дело, что id не должен меняться, а при наличии сеттера id задачи можно изменить в любой момент

    public Task(String title, String text, StatusOfTask status) {
        this.title = title;
        this.text = text;
        this.status = status;
    }

    public void setId(int id){ // Работает только паблик, а это странно для ID
        this.id = id;
    }

    public void setStatus(StatusOfTask status){
        this.status = status;
    }

    public int getId(){
        return id;
    }

    public StatusOfTask getStatus(){
        return status;
    }

    public String getTitle () {
        return title;
    }

    public String getText(){
        return text;
    }

    @Override
    public String toString() {
        return "ID - " + id + " Задача: " + title + " Статус: " + status + "\nОписание: " + text
                + "\n------------------------------------------------";
    }

    // Непонятен смысл. Мы ищем по ID, а для чисел методы уже переопределены
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(text, task.text) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, status);
    }

}
