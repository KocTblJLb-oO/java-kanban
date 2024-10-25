public class Subtask extends Task{
    int parentID;

    public Subtask(int parentEpicID, int id, String title, String text, StatusOfTask status) {
        super(id, title, text, status);
        this.parentID = parentEpicID;
    }

    @Override
    public String toString() {
        return "ID - " + id + " Подзадача: " + title + " Статус: " + status + "\nОписание: " + text + "\nЭпик: " + parentID
                + "\n------------------------------------------------";
    }
}
