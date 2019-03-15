package atm;
import java.util.ArrayList;
import java.util.List;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class Main extends Application{
	int ID;		
	Stage menu;
	Stage operation;
	Scene loginScene, mainScene, depositScene, withdrawScene;
	static boolean exitConfirm;
	Button keypadArray[] = new Button[10];
	Button keypadArray1[] = new Button[10];
	List<Transactions> history = new ArrayList<Transactions>();
	int historyIndex=-1, x=-1;
	
	public static void main(String[] args) {		
			launch(args);
		}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		menu = primaryStage;
		menu.setTitle("simple atm");
		//loginScene
		Label IDLabel = new Label("ID: ");	
		TextField IDInput = new TextField();
        Button loginButton = new Button("Log In");
        IDInput.setPromptText("enter ID here");			//appears grayed out in the field
        
        loginButton.setOnAction(e -> {
        	if(isInt(IDInput.getText()))
        		menu.setScene(mainScene);
        	else 
        		{
        		alertBox("error!", "please enter a number");
        		IDInput.setText("");
        		}
        });
        GridPane loginLayout = new GridPane();
		loginLayout = gridSetup(loginLayout);
        GridPane.setConstraints(IDLabel, 0, 0);
        GridPane.setConstraints(IDInput, 1, 0);
        GridPane.setConstraints(loginButton, 1, 1);
        loginLayout.getChildren().addAll(IDLabel, IDInput, loginButton);
        
        //mainScene
		Label label1 = new Label("");						
		Button depositButton = new Button("deposit");		
		Button withdrawButton = new Button("withdraw");		
		Button checkBalanceButton = new Button("check balance");
		Button historyButton = new Button("history");
		Button previousButton = new Button("prev");
		Button nextButton = new Button("next");
		
		label1.setMinSize(100, 50);
		depositButton.setMinSize(100, 50);
		withdrawButton.setMinSize(100, 50);
		checkBalanceButton.setMinSize(100, 50);
		historyButton.setMinSize(100, 50);
		previousButton.setMinSize(100, 50);
		nextButton.setMinSize(100, 50);
		
		depositButton.setOnAction(e -> menu.setScene(depositScene));	//stageName.setScene(sceneName) to switch to another scene
		withdrawButton.setOnAction(e -> menu.setScene(withdrawScene));
		checkBalanceButton.setOnAction(e -> {
			label1.setText(Long.toString(Atm.getBalance()));
			addToHistory("balance check", Long.toString(Atm.getBalance()));
		});
		historyButton.setOnAction(e -> {
			if(historyIndex == -1)
				alertBox("error!", "history is empty");
			else
				label1.setText(history.get(historyIndex).getType() + " " + (history.get(historyIndex).getAmount())) ;
		});
		nextButton.setOnAction(e -> {
			if(historyIndex >= x)
				alertBox("error!", "theres no next");
			else
			{
				historyIndex++;
				label1.setText(history.get(historyIndex).getType() + " " + history.get(historyIndex).getAmount());
			}
		});
		previousButton.setOnAction(e -> {
			if(historyIndex-1 == -1 || historyIndex == -1)
				alertBox("error!", "theres no previous");
			else
			{
				historyIndex--;
				label1.setText(history.get(historyIndex).getType() + " " + history.get(historyIndex).getAmount());
			}
		});
		
		GridPane grid = new GridPane();
		grid = gridSetup(grid);
		GridPane.setConstraints(label1, 1, 1);					//Gridpane.setconstraints or grid.getChildren().add to set position on the grid
		GridPane.setConstraints(depositButton, 0, 0);
		GridPane.setConstraints(withdrawButton, 1, 0);
		GridPane.setConstraints(checkBalanceButton, 2, 0);
		GridPane.setConstraints(historyButton, 0, 2);
		GridPane.setConstraints(previousButton, 1, 2);
		GridPane.setConstraints(nextButton, 2, 2);
		grid.getChildren().addAll(label1, depositButton, withdrawButton, checkBalanceButton, historyButton, previousButton, nextButton);	//adding all the objects to the scene
		//depositScene
		GridPane depositLayout = new GridPane();
		depositLayout = gridSetup(depositLayout);
		
		Label depositLabel1 = new Label("enter amount");	GridPane.setConstraints(depositLabel1, 3,0);
		TextField amountInput = new TextField();	GridPane.setConstraints(amountInput, 3,1);
        amountInput.setPromptText("enter amount here");
        
        for( int i =0; i<10; i++) {
        	String j=Integer.toString(i);
        	keypadArray[i] = new Button(j);
        	keypadArray[i].setMinSize(50, 30);
        	keypadArray[i].setOnAction(e-> amountInput.setText(amountInput.getText()+j));
        	depositLayout.add(keypadArray[i], 0 , 0);
        }
        Button enter = new Button("enter");		
		Button delete = new Button("delete");
		Button back = new Button("back");
		enter.setOnAction(e -> {
			if(isInt(amountInput.getText()))
			{
				Atm.deposit(Integer.parseInt(amountInput.getText()));
			alertBox("success", "deposited");
			addToHistory("deposit", amountInput.getText());
			if(confirmBox("another transaction", "do u wanna make another transaction?"))
				menu.setScene(mainScene);
			else
				closeProgram();
			}
		else
			alertBox("error!", "please enter an integer");
		amountInput.setText("");
		});
		delete.setOnAction(e -> amountInput.setText(""));
		back.setOnAction(e -> menu.setScene(mainScene));
        
        GridPane.setConstraints(keypadArray[0], 1, 3);
    	GridPane.setConstraints(keypadArray[1], 0, 2);
		GridPane.setConstraints(keypadArray[2], 1, 2);
		GridPane.setConstraints(keypadArray[3], 2, 2);
		GridPane.setConstraints(keypadArray[4], 0, 1);
		GridPane.setConstraints(keypadArray[5], 1, 1);
		GridPane.setConstraints(keypadArray[6], 2, 1);
		GridPane.setConstraints(keypadArray[7], 0, 0);
		GridPane.setConstraints(keypadArray[8], 1, 0);
		GridPane.setConstraints(keypadArray[9], 2, 0);
		GridPane.setConstraints(enter, 2, 3);	enter.setMinSize(50, 30);
		GridPane.setConstraints(delete, 3, 2);	delete.setMinSize(50, 30);
		GridPane.setConstraints(back, 0, 3);	back.setMinSize(50, 30);
		
		depositLayout.getChildren().addAll(depositLabel1, amountInput, enter, delete, back);
        depositScene = new Scene(depositLayout, 400, 300);
        
      //withdrawScene
      	GridPane withdrawLayout = new GridPane();
      	withdrawLayout = gridSetup(depositLayout);
      		
      	Label withdrawLabel1 = new Label("enter amount");	GridPane.setConstraints(withdrawLabel1, 3,0);
      	TextField Input = new TextField();	GridPane.setConstraints(Input, 3,1);
        Input.setPromptText("enter amount here");
              
        for( int i =0; i<10; i++) {
        	
        	String j=Integer.toString(i);
            keypadArray1[i] = new Button(j);
            keypadArray1[i].setMinSize(50, 30);
            keypadArray1[i].setOnAction(e-> Input.setText(Input.getText()+j));
            withdrawLayout.add(keypadArray1[i], 0 , 0);
            }
        Button enter1 = new Button("enter");		
        Button delete1 = new Button("delete");
        Button back1 = new Button("back");
      	enter1.setOnAction(e -> {
      		if(isInt(Input.getText()))
      		{
      			if(Long.parseLong(Input.getText())<=Atm.getBalance())
      			{
      				Atm.withdraw(Long.parseLong(Input.getText()));
      	      		alertBox("success", "withdrawn");
      	      		addToHistory("withdraw", Input.getText());
      	      		if(confirmBox("another transaction", "do u wanna make another transaction?"))
      	      			menu.setScene(mainScene);
      	      		else
      	      			closeProgram();
      			}
      			else
      				alertBox("error!", "insufficient credits");
      				Input.setText("");
      		}
      		else
      			alertBox("error!", "please enter an integer");
      		Input.setText("");
      		});
      	delete1.setOnAction(e -> Input.setText(""));
      	back1.setOnAction(e -> menu.setScene(mainScene));
              
        GridPane.setConstraints(keypadArray1[0], 1, 3);
        GridPane.setConstraints(keypadArray1[1], 0, 2);
        GridPane.setConstraints(keypadArray1[2], 1, 2);
      	GridPane.setConstraints(keypadArray1[3], 2, 2);
      	GridPane.setConstraints(keypadArray1[4], 0, 1);
  		GridPane.setConstraints(keypadArray1[5], 1, 1);
   		GridPane.setConstraints(keypadArray1[6], 2, 1);
     	GridPane.setConstraints(keypadArray1[7], 0, 0);
    	GridPane.setConstraints(keypadArray1[8], 1, 0);
      	GridPane.setConstraints(keypadArray1[9], 2, 0);
      	GridPane.setConstraints(enter1, 2, 3);	enter1.setMinSize(50, 30);
      	GridPane.setConstraints(delete1, 3, 2);	delete1.setMinSize(50, 30);
      	GridPane.setConstraints(back1, 0, 3);	back1.setMinSize(50, 30);
      		
      	withdrawLayout.getChildren().addAll(depositLabel1, Input, enter1, delete1, back1);
        withdrawScene = new Scene(withdrawLayout, 400, 300);
        
		mainScene = new Scene(grid, 500, 500);
		
        loginScene = new Scene(loginLayout, 300, 200);
        menu.setScene(loginScene);
		menu.show();
		
		menu.setOnCloseRequest(e -> {
			e.consume();		//cant exit using the X
			closeProgram();
		});
	}
	
	public void addToHistory(String type, String amount){
		Transactions transaction = new Transactions();
		transaction.type = type;
		transaction.amount = amount;
		if(history.size()==5)
			history.remove(0);
		history.add(transaction);
		historyIndex++;
		x++;
	}
		
	public GridPane gridSetup (GridPane gridName) {
		gridName = new GridPane();
		gridName.setPadding(new Insets(10, 10, 10, 10));			//adjusts space from the edges
		gridName.setVgap(8);
		gridName.setHgap(10);
		gridName.setAlignment(Pos.CENTER);
		return gridName;
	}
	
	//isInt
	private boolean isInt(String input){
        try{
            Long.parseLong(input);
            return true;
        }catch(NumberFormatException e){
        	return false;
        }
    }
	
	//alertBox
	public static void alertBox(String title, String message) {
		Stage alert = new Stage();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setTitle(title);
		alert.setMinHeight(200);
		alert.setMinWidth(250);
		
		Label label = new Label(message);
		VBox layout = new VBox();
		layout.getChildren().add(label);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		alert.setScene(scene);
		alert.showAndWait();
	}
	
	//confirmBox
	public static boolean confirmBox(String title, String message) {
    	Stage confirmWindow = new Stage();
    	confirmWindow.initModality(Modality.APPLICATION_MODAL);	//prevents interaction with other windows till this ones closed
    	confirmWindow.setTitle(title);
    	confirmWindow.setMinWidth(250);
    	confirmWindow.setMinHeight(200);
    	
        Label label = new Label(message);
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
        	exitConfirm = true;
        	confirmWindow.close();
        });
        noButton.setOnAction(e -> {
        	exitConfirm = false;
        	confirmWindow.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        confirmWindow.setScene(scene);
        confirmWindow.showAndWait();				//used with modality

        return exitConfirm;
    }
    
    private void closeProgram() {
    	confirmBox("confirm box", "are u sure u wanna exit");
    	if(exitConfirm)
    		menu.close();
    }
}