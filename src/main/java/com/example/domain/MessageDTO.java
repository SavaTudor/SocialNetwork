package com.example.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.example.build.Build.formatter;

public class MessageDTO extends Entity<Integer>{
    private int from;
    private List<Integer> to;
    private String message;
    private LocalDateTime data;
    private Integer reply;

    /**
     * Constructor
     * @param from User representing the sender of the message
     * @param to List of User representing  the receiver of the message
     * @param message String representing the message
     */
    public MessageDTO(int from, List<Integer> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = LocalDateTime.now();
        reply = 0;
    }

    /**
     * Getter for the sender of the message
     * @return User representing the receiver of the message
     */
    public int getFrom() {
        return from;
    }

    /**
     * Setter for the sender of the message
     * @param from User representing the new sender
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * Getter for the receiver of the message
     * @return List of User representing the receiver of the message
     */
    public List<Integer> getTo() {
        return to;
    }

    /**
     * Setter for the receiver of the message
     * @param to List of User representing the new receiver
     */
    public void setTo(List<Integer> to) {
        this.to = to;
    }


    /**
     * Getter for the message
     * @return String representing the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for the message
     * @param message String representing the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for the date
     * @return LocalDataTime representing the date that the message has been sent
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * Setter for the date
     * @param data LocalDataTime representing the new date
     */
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    /**
     * Getter for Reply
     * @return Message representing the message being answered
     */
    public Integer getReply() {
        return reply;
    }

    /**
     * Setter for Reply
     * @param reply representing the message being answered
     */
    public void setReply(Integer reply) {
        this.reply = reply;
    }

    /**
     * Gets the string format of the message
     *
     * @return String representing the format of the message
     */
    @Override
    public String toString() {
        if(reply == null)
            return "Message{" +
                    "id=" + super.getId() +
                    " from=" + from +
                    ", to=" + to +
                    ", message='" + message + '\'' +
                    ", data=" + data.format(formatter) +
                    '}';
        return "Message{" +
                "id=" + super.getId() +
                " from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", data=" + data.format(formatter) +
                ", reply=" + reply +
                '}';
    }

    /**
     * Check if two message are equal
     * @param o another Object
     * @return true if the messages have the same id , false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return super.getId().equals(message1.getId());
    }

    /**
     * HashCode
     * @return Integer representing the HashCode of the message
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), from, to, message);
    }
}
