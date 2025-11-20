class Tool extends Model{
  int toolID;
  string type;
  int toolCondition;//moderate priority
  string whoHas;
  string damageReport;//low priority
  string timeBooked;
  string durationBooked;

  tool(){
        toolID = 00000;//figure out how to have a constantly updating variable
        type = "none";
        toolCondition = 0;
        whoHas = "n/a";
        damageReport = "n/a";
        timeBooked = "n/a";
        durationBooked = "n/a";
    };
  public void addTool();
  public void removeTool();
  public void modifyTool();
}
