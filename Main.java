import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

class Main {
  public static void main(String[] args) {
    System.out.println("===================================================================");
    System.out.println("###  ## # #  #  ### ### ###     # # ### ### ### ### # # # # ### # #");
    System.out.println("#   #   # #  #  #   #   #       # # #   # # #   #   ### ##  #   ###");
    System.out.println("### #   ###  #  ### ### ###     ### ### ### ### ### ### #   ### ###");
    System.out.println("  # #   # #  #  #   #   #       ### #   ##    # #   ### ##  #   ###");
    System.out.println("###  ## # #  #  #   #   ###      #  ### # # ### ### # # # # ### # #");
    System.out.println("===================================================================");
    System.out.println("A game by Karsten Römling and Jonthan Hölzer");
    Settings.addSetting(new Setting("Map is structured by a grid", "OFF", "boolean"));
    Settings.addSetting(new Setting("Number of ships with a length of 1", "4", "number"));
    Settings.addSetting(new Setting("Number of ships with a length of 2", "3", "number"));
    Settings.addSetting(new Setting("Number of ships with a length of 3", "2", "number"));
    Settings.addSetting(new Setting("Number of ships with a length of 4", "1", "number"));
    Settings.addSetting(new Setting("Number of ships with a length of 5", "0", "number"));
    Settings.addSetting(new Setting("An additional navigation request after menu option execution", "OFF", "boolean"));
    new Menu();
  }
}

class Menu{
  public boolean running = true;
  public String[] options = {
    "Play Game",
    "Developer options",
    "Available commands",
    "Settings"
  };

  public Menu(){
    draw();
    while(running){
      if(Settings.getSetting("7").getBoolValue()){
        System.out.println("\nEnter a command to navigate (\"-leave\", \"-menu\" etc.):");
        System.out.print("> ");
        String input = System.console().readLine();
        switch(input){
          case "-leave":
            bye();
            running = false;
            break;
          case "-menu":
            draw();
            break;
          case "-help":
            help();
            break;
        }
      }else{
        draw();
      }
    }
  }

  public void help(){
    String[] str = {
      "This is the help menu. Contents will be added soon."
    };
    new Box("HELP",str);
  }

  public void draw(){
    System.out.println("\n\n");
    String[] optionsModified = new String[options.length];
    for(int i = 0; i < options.length; i++){
      optionsModified[i] = String.valueOf(i+1)+" "+options[i];
    }
    new Box("MENU", optionsModified);
    System.out.print("\n\n");
    System.out.println("What do you want to do?");
    System.out.print("> ");
    String input = System.console().readLine();
    doThis(input);
  }

  public void doThis(String what){
    System.out.print("\n\n\n");
    switch(what.toLowerCase()){
      case "1":
      case "play game":
        Meer m = new Meer();
        m.playGame();
        break;
      case "2":
      case "developer options":
        Meer meer = new Meer();
        new Box("DEV",meer.draw(meer.hiddenK));
        break;
      case "3":
      case "available commands":
        String[] str3 = {
          "GLOBAL COMMANDS (used in the menu)",
          "-leave: Leave the app",
          "-menu: Open the menu",
          "-help: Open a help menu",
          "","LOCAL COMMANDS (used elsewhere)",
          "-cancel: Used to cancel your game in \"Play Game\"",
          "-finish: Used to leave the settings menu"
        };
        new Box("COMMAND HELP",str3);
        break;
      case "4":
      case "settings":
        Settings.drawSettingsMenu();
        break;
      case "-leave":
        bye();
        running = false;
        break;
      case "-menu":
        draw();
        break;
      case "-help":
        help();
        break;
      default:
        System.out.println("Sorry, but your input couldn't be associated to an option.");
        System.out.println("Please try again:");
        draw();
    }
  }

  public void bye(){
    System.out.println("\n\n");
    System.out.println("===============");
    System.out.println("### # # ###  # ");
    System.out.println("# # # # #    # ");
    System.out.println("### ### ###  # ");
    System.out.println("# #  #  #      ");
    System.out.println("###  #  ###  # ");
    System.out.println("===============");
  }
}

class Box{
  public String title;
  public String[] text;
  public int titleLen;
  private int width = 80;

  public Box(String ti, String[] te){
    title = ti;
    text = te;
    titleLen = title.length();
    System.out.println("  "+oneCharXTimes('_',titleLen+4));
    System.out.println(" |"+oneCharXTimes(' ',titleLen+4)+"|   "+oneCharXTimes('=', width-8-titleLen));
    System.out.println(" |  "+title+"  |   "+oneCharXTimes('=', width-8-titleLen));
    System.out.println(" |"+oneCharXTimes('_',titleLen+4)+"|   "+oneCharXTimes('=', width-8-titleLen));
    System.out.println(" |");
    for(int i = 0; i < text.length; i++){
      System.out.println(" | "+text[i]);
    }
    System.out.println(" |"+oneCharXTimes('_',width));
  }

  public static String oneCharXTimes(char ch, int x){
    String result = "";
    for(int i = 0; i < x; i++){
      result += ch;
    }
    return result;
  }
}

class Setting{
  public String description;
  public String defaultV;
  public String type;
  public String value;

  public Setting(String descr, String deflt, String t){
    description = descr;
    defaultV = deflt;
    type = t;
    value = defaultV;
  }

  public String draw(){
    return description+": "+value;
  }

  public boolean check(String newVal){
    boolean result = true;
    switch(type){
      case "boolean":
        if(!(newVal.equals("ON") || newVal.equals("OFF"))){
          result = false;
        }
        break;
      case "number":
        if(!Settings.isNumber(newVal)){
          result = false;
        }
        break;
    }
    return result;
  }

  public int getNumberValue(){
    if(type.equals("number")){
      return Integer.parseInt(value);
    }else{
      return -1;
    }
  }

  public boolean getBoolValue(){
    if(type.equals("boolean") && value.equals("ON")){
      return true;
    }else{
      return false;
    }
  }
}

class Settings{
  public static ArrayList<Setting> settingsL = new ArrayList<Setting>();

  private static String[] draw(){
    String[] result = new String[settingsL.size()];
    for(int i = 0; i < settingsL.size(); i++){
      result[i] = String.valueOf(i+1)+" "+settingsL.get(i).draw();
    }
    return result;
  }

  public static void addSetting(Setting setting){
    settingsL.add(setting);
  }

  public static Setting getSetting(String ip){
    String num = "";
    int i = 1;
    if(!isNumber(ip.substring(0,i))){
      return null;
    }
    while(isNumber(ip.substring(0,i))){
      num = ip.substring(0,i);
      if(i+1 > ip.length()){
        break;
      }
      i++;
    }
    int index = Integer.parseInt(num)-1;
    return settingsL.get(index);
  }

  public static boolean isNumber(String val){
    try{
      int x = Integer.parseInt(val);
    }catch(Exception e){
      return false;
    }
    return true;
  }

  public static void reset(){
    for(int i = 0; i < settingsL.size(); i++){
      settingsL.get(i).value = settingsL.get(i).defaultV;
    }
  }

  public static void drawSettingsMenu(){
    String input = "";
    while(!input.equals("-finish")){
      new Box("SETTINGS", draw());
      while(true){
        System.out.println("\n | To change a setting, select it and enter the new value (e.g. \"1 OFF\")");
        System.out.println(" | To reset a setting, select it and enter \"-reset\" or \"-default\" (e.g. \"1 -reset\")");
        System.out.println(" | To reset all settings, enter \"* -reset\" or \"* -default\"");
        System.out.println(" | Leave the settings menu by inputting \"-finish\"");
        System.out.print(" | > ");
        input = System.console().readLine();
        if(input.equals("-finish")){
          break;
        }else if(input.equals("* -reset") || input.equals("* -default")){
          reset();
          break;
        }else{
          if(getSetting(input) == null){
            System.out.println("\n | Wrong input! Try again!");
            continue;
          }
          int index = String.valueOf(settingsL.indexOf(getSetting(input))).length() + 1;
          if(input.substring(index-1,index)==" "){
            System.out.println("\n | Wrong input! Try again!");
            continue;
          }
          Setting s = getSetting(input);
          switch(input.substring(index)){
            case "-reset":
            case "-default":
              s.value = s.defaultV;
              break;
            default:
              if(s.check(input.substring(index))){
                s.value = input.substring(index);
              }else{
                System.out.println("\n | Wrong input! Try again!");
                continue;
              }
              break;
          }
          break;
        }
      }
    }
  }
}

class Meer{
  public boolean[][] hiddenK = new boolean[12][12];
  public boolean[][] userK = new boolean[12][12];
  private Random r;
  public int round = 1;
  public int points = 0;
  public double precision = 0;

  public Meer(){
    for(int y = 0; y < 11; y++){
      for(int x = 0; x < 11; x++){
        hiddenK[x][y] = false;
        userK[x][y] = false;
      }
    }
    r = new Random();
    addBoats(4,Settings.getSetting("6").getNumberValue());
    addBoats(4,Settings.getSetting("5").getNumberValue());
    addBoats(3,Settings.getSetting("4").getNumberValue());
    addBoats(2,Settings.getSetting("3").getNumberValue());
    addBoats(1,Settings.getSetting("2").getNumberValue());
  }

  public void playGame(){
    String[] str = {"A","B","C","D","E","F","G","H","I","J","K","L"};
    String[] info = {
      "Welcome onto our ship!",
      "Let's play \"Schiffe versenken\"!",
      "Here is the map of the sea we are currently discovering.",
      "There are 4 little ships (1 unit), 3 intermediately big ships (2 units)",
      "2 big ships (3 units) and one huge ship (4 units).",
      "Shoot somewhere by entering the coordinates of where you expect a ship to be.",
      "To do so, you will be required to use this format: \"A1\".",
      "If you've hit a ship or a part of it, an \"X\" will occure on the position.",
      "Otherwise, there'll be a \"·\".",
      "You can always cancel the game by inputting \"-cancel\".",
      "LET'S START!"
    };
    new Box("PLAY GAME - INFO",info);
    System.out.println("\n\n | Press Enter to start:");
    System.out.print(" | > ");
    String noname = System.console().readLine();
    boolean running = true;
    while(running){
      new Box("PLAY GAME - ROUND "+String.valueOf(round), draw(userK));
      boolean err;
      do{
        err = false;
        System.out.println("\n\n | Enter the coordinate (e.g. A1):");
        System.out.print(" | > ");
        String ip = System.console().readLine();
        if(ip.equals("-cancel")){
          running = false;
        }else{
          try{
            int x = Integer.parseInt(ip.substring(1))-1;
            int y = Arrays.asList(str).indexOf(ip.substring(0, 1));
            userK[x][y] = true;
            if(hiddenK[x][y]){
              points++;
            }
            if(points>19){
              System.out.println("\n | YOU HAVE WON!\n | WELL DONE!");
              running = false;
            }
          }catch(Exception e){
            err = true;
            System.out.println("\n | Wrong input! Try it again!");
          }
        }
      }while(err);
      precision = Math.round((double)points/(double)round*1000.0)/10;
      round++;
    }
  }

  public void addBoats(int length, int number){
    int i = 0;
    while(i < number){
      int x = r.nextInt(12);
      int y = r.nextInt(12);
      if(r.nextInt(2)==0){
        if(tryGetAt(x, y, length-1,0)){
          i++;
        }
      }else{
        if(tryGetAt(x, y, 0, length-1)){
          i++;
        }
      }
    }
  }

  public boolean tryGetAt(int x, int y, int dirX, int dirY){
    for(int iX = 0; iX <= dirX; iX++){
      for(int iY = 0; iY <= dirY; iY++){
        if(!(iX+x > 11 || iX+x < 0 || iY+y > 11 || iY+y < 0)){
          if(hiddenK[x+iX][y+iY]){
            return false;
          }
        }
        else{
          return false;
        }
      }
    }
    for(int iX = 0; iX <= dirX; iX++){
      for(int iY = 0; iY <= dirY; iY++){
        hiddenK[iX+x][iY+y] = true;
      }
    }
    return true;
  }

  public String[] draw(boolean[][] meerK){
    String[] str = {"A","B","C","D","E","F","G","H","I","J","K","L"};
    String result = "\n";
    for(int y = 0; y < 15; y++){
      result += "  ";
      for(int x = 0; x < 15; x++){
        if(y == 0){
          if(x > 1 && x < 14){
            String space = "";
            if(x-1 < 10){
              space = " ";
            }
            result += String.valueOf(x-1)+space;
          }else{
            result += "  ";
          }
        }else if(x == 0){
          if(y > 1 && y < 14){
            result += str[y-2]+" ";
          }else{
            result += "  ";
          }
        }else if(y == 1 || y == 14 || x == 1 || x == 14){
          result += "+ ";
        }else{
          if(meerK[x-2][y-2] && hiddenK[x-2][y-2]){
            result += "X ";
          }else if(meerK[x-2][y-2]){
            result += "· ";
          }else{
            result += "  ";
          }
        }
        if(Settings.getSetting("1").getBoolValue()){
          result += "| ";
        }
      }
      switch(y){
        case 1:
          result += "      Round: "+String.valueOf(round);
          break;
        case 2:
          result += "      Points: "+String.valueOf(points);
          break;
        case 3:
          result += "      Precision: "+String.valueOf(precision)+"%";
          break;
      }
      result += "\n";
      if(Settings.getSetting("1").getBoolValue()){
        result += "  "+Box.oneCharXTimes('-', 58)+"\n";
      }
    }
    result += " \n ";
    return result.split("\n");
  }
}