package ru.moysayt.steptraker.model;

public class Subtask extends Task{
   /* private int id;
    private String title;
    private String text;
    private  StatusOfTask status;*/
    private int parentId;

    public Subtask(int parentEpicID, String title, String text, StatusOfTask status) {
        super(title, text, status);
        /*this.title = title;
        this.text = text;
        this.status = status;*/
        this.parentId = parentEpicID;
    }

   /* public void setId(int id){ // Работает только паблик, а это странно для ID
        this.id = id;
    }*/

    public int getParentId(){
        return parentId;
    }

//    public void setStatus(StatusOfTask status){
//        setStatus(status);
//    }

    @Override
    public String toString() {
        return "ID - " + getId() + " Подзадача: " + getTitle() + " Статус: " + getStatus() + "\nОписание: " + getText()
                + "\nЭпик: " + parentId + "\n------------------------------------------------";
    }
}
