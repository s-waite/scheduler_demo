package dev.sam.scheduler.model;

public class Contact {
   private int id;
   private String contactName;
   private String email;

   @Override
   public String toString() {
      return this.contactName;
   }

   public int getId() {
      return id;
   }

   public String getContactName() {
      return contactName;
   }

   public String getEmail() {
      return email;
   }

   public Contact(Integer id, String contactName, String email) {
      this.id = id;
      this.contactName = contactName;
      this.email = email;
   }
}
