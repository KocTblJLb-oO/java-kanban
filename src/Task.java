public class Task {
    final int id;
    String title;
    String text;
    StatusOfTask status;

    public Task(int id, String title, String text, StatusOfTask status) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.status = status;
    }

    @Override
    public String toString() {
        return "ID - " + id + " Задача: " + title + " Статус: " + status + "\nОписание: " + text
                + "\n------------------------------------------------";
    }
}
