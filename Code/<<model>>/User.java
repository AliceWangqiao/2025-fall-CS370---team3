class User extends Model{
  //this might be the private/protected/public part of the class
  string Username;
  string Password;
  bool role;//1 for admin, 0 for user
  string fullName;
  string email;
  int[] toolsBooked;

  user(){
      Username = "default";
      Password = "default";
      role= 0;
      fullName = "default';
      email = "default";
      int[] toolsBooked=new int[20]
  };
  public void addUser();
  public void removeUser();
  public modifyUser();
}
