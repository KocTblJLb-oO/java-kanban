import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> mySubtaskID = new ArrayList<>();

    public Epic(int id, String title, String text, StatusOfTask status) {
        super(id, title, text,status);
    }

    public void addSubtask(int idSubtask){

    }

    public void setStatus(TaskManager t){
        StatusOfTask status;
        int all = 0;
        int newSub = 0;
        int done = 0;

        for (int subtaskID : mySubtaskID){
           status = t.subtaskList.get(subtaskID).status;
           all++;

           if (status == StatusOfTask.NEW || all == 0){
               newSub++;
           } else if (status == StatusOfTask.DONE) {
               done++;
           }

           if (all == newSub){
               this.status = StatusOfTask.NEW;
           } else if (all == done) {
               this.status = StatusOfTask.DONE;
           } else {
               this.status = StatusOfTask.IN_PROGRESS;
           }
        }
    }

    @Override
    public String toString() {
        return "ID - " + id + " Эпик: " + title + " Статус: " + status + "\nОписание: " + text + "\nПодзадачи:" + mySubtaskID
                + "\n------------------------------------------------";
    }
}
