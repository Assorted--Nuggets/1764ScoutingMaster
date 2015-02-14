/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;

/**
 *
 * @author mdow7299
 */
public class Scout extends Application
{

    public static GridPane mainGrid;
    public static GridPane topGrid;
    public static GridPane autonomousGrid;
    public static GridPane teleopGrid;
    public static GridPane ratingGrid;

    public static ScrollPane mainPane;

    public static TextField teamNumTxt, toteScored, binStacked, noodleInserted, telTotes, telBins, telNoodles, roundNumber;

    public static Label mobileLbl, noShowLbl, isMovingLbl;
    public static CheckBox mobile, noShow, isMoving, liftTotes, liftBins, liftNoodles;
    public static Slider teamwork, security, durability;
    public static Button exportBtn;
    public static FileChooser importer;

    public static ServerSocket server;
    public static Socket client;
    public static ObjectInputStream input;

    @Override
    public void start(final Stage primaryStage)
    {
        primaryStage.setTitle("1764 Scouting");
        importer = new FileChooser();

        importer.getExtensionFilters().add(new FileChooser.ExtensionFilter("SDB", "*.sdb"));

        initGrids();
        addTop();
        addAutonomous();
        addTeleop();
        addRating();

        exportBtn = new Button("Import");
        exportBtn.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {

                SwingUtilities.invokeLater(
                        new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    server = new ServerSocket(3434);
                                    client = server.accept();
                                    input = new ObjectInputStream(client.getInputStream());
                                } catch (IOException ex)
                                {
                                    Logger.getLogger(Scout.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                );

                importSDB();
            }

        });
        mainGrid.add(exportBtn, 0, 6);

        mainPane = new ScrollPane();
        mainPane.setContent(mainGrid);
        Scene scene = new Scene(mainPane, 800, 600);

        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void initGrids()
    {
        mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.TOP_LEFT);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));

        topGrid = new GridPane();
        topGrid.setAlignment(Pos.TOP_LEFT);
        topGrid.setHgap(10);
        topGrid.setVgap(10);
        topGrid.setPadding(new Insets(25, 25, 25, 25));

        autonomousGrid = new GridPane();
        autonomousGrid.setAlignment(Pos.TOP_LEFT);
        autonomousGrid.setHgap(10);
        autonomousGrid.setVgap(10);
        autonomousGrid.setPadding(new Insets(25, 25, 25, 25));

        teleopGrid = new GridPane();
        teleopGrid.setAlignment(Pos.TOP_LEFT);
        teleopGrid.setHgap(10);
        teleopGrid.setVgap(10);
        teleopGrid.setPadding(new Insets(25, 25, 25, 25));

        ratingGrid = new GridPane();
        ratingGrid.setAlignment(Pos.TOP_LEFT);
        ratingGrid.setHgap(10);
        ratingGrid.setVgap(10);
        ratingGrid.setPadding(new Insets(25, 25, 25, 25));
    }

    public static void addTop()
    {
        teamNumTxt = new TextField();
        teamNumTxt.setPromptText("Team Number");
        topGrid.add(teamNumTxt, 0, 0);

        mobileLbl = new Label("Mobile?");
        topGrid.add(mobileLbl, 4, 0);

        mobile = new CheckBox();
        topGrid.add(mobile, 5, 0);

        noShowLbl = new Label("No Show?");
        topGrid.add(noShowLbl, 6, 0);
        noShow = new CheckBox();
        topGrid.add(noShow, 7, 0);

        roundNumber = new TextField();
        roundNumber.setPromptText("Round Number");
        topGrid.add(roundNumber, 20, 0);

        mainGrid.add(topGrid, 0, 0);
    }

    public static void addAutonomous()
    {
        Text autonomous = new Text("AUTONOMOUS");
        autonomous.setFont(Font.font("Tahoma", FontWeight.LIGHT, 20));
        mainGrid.add(autonomous, 0, 1);

        toteScored = new TextField();
        toteScored.setPromptText("Totes Scored");
        autonomousGrid.add(toteScored, 0, 0);

        binStacked = new TextField();
        binStacked.setPromptText("Bins Scored");
        autonomousGrid.add(binStacked, 1, 0);

        noodleInserted = new TextField();
        noodleInserted.setPromptText("Noodles Scored");
        autonomousGrid.add(noodleInserted, 2, 0);

        isMovingLbl = new Label("Is Moving?");
        autonomousGrid.add(isMovingLbl, 4, 0);
        isMoving = new CheckBox();
        autonomousGrid.add(isMoving, 5, 0);

        mainGrid.add(autonomousGrid, 0, 2);
    }

    public static void addTeleop()
    {
        Text teleopText = new Text("TELEOPERATED");
        teleopText.setFont(Font.font("Tahoma", FontWeight.LIGHT, 20));
        mainGrid.add(teleopText, 0, 3);

        telTotes = new TextField();
        telTotes.setPromptText("Totes Scored");
        teleopGrid.add(telTotes, 0, 0);

        telBins = new TextField();
        telBins.setPromptText("Bins Scored");
        teleopGrid.add(telBins, 1, 0);

        telNoodles = new TextField();
        telNoodles.setPromptText("Noodles Scored");
        teleopGrid.add(telNoodles, 2, 0);

        Label liftTotesLbl = new Label("Lift Totes?");
        teleopGrid.add(liftTotesLbl, 0, 1);
        liftTotes = new CheckBox();
        teleopGrid.add(liftTotes, 1, 1);

        Label liftBinsLbl = new Label("Lift Bins?");
        teleopGrid.add(liftBinsLbl, 0, 2);
        liftBins = new CheckBox();
        teleopGrid.add(liftBins, 1, 2);

        Label liftNoodlesLbl = new Label("Lift Noodles?");
        teleopGrid.add(liftNoodlesLbl, 0, 3);
        liftNoodles = new CheckBox();
        teleopGrid.add(liftNoodles, 1, 3);

        mainGrid.add(teleopGrid, 0, 4);
    }

    public static void addRating()
    {
        Text rating = new Text("Team Ratings");
        Text toolTip = new Text("(1 = Bad, 5 = Good)");
        rating.setFont(Font.font("Tahoma", FontWeight.LIGHT, 20));
        toolTip.setFont(Font.font("Tahoma", FontWeight.LIGHT, 20));
        ratingGrid.add(toolTip, 1, 0);
        ratingGrid.add(rating, 0, 0);
        Label teamworkLbl = new Label("Stacking Efficiency");
        teamwork = new Slider();
        teamwork.setMin(1);
        teamwork.setMax(5);

        teamwork.setSnapToTicks(true);
        teamwork.setMajorTickUnit(1);
        teamwork.setMinorTickCount(0);
        teamwork.setShowTickLabels(true);
        teamwork.setShowTickMarks(true);

        Label securityLbl = new Label("Security of Bins");
        security = new Slider();
        security.setMin(1);
        security.setMax(5);

        security.setSnapToTicks(true);
        security.setMajorTickUnit(1);
        security.setMinorTickCount(0);
        security.setShowTickLabels(true);
        security.setShowTickMarks(true);

        ratingGrid.add(securityLbl, 0, 2);
        ratingGrid.add(security, 1, 2);

        Label durabilityLbl = new Label("Durability");
        durability = new Slider();
        durability.setMin(1);
        durability.setMax(5);

        durability.setSnapToTicks(true);
        durability.setMajorTickUnit(1);
        durability.setMinorTickCount(0);
        durability.setShowTickLabels(true);
        durability.setShowTickMarks(true);

        ratingGrid.add(durabilityLbl, 0, 3);
        ratingGrid.add(durability, 1, 3);

        ratingGrid.add(teamworkLbl, 0, 1);
        ratingGrid.add(teamwork, 1, 1);

        mainGrid.add(ratingGrid, 0, 5);
    }

    public static void importSDB()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new File("temp.sdb"));

            String line;

            while (!((line = (String) input.readObject()).equals("end")))
            {
                writer.println(line);
            }

            writer.close();
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Scout.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Scout.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Scout.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File("temp.sdb")));

            String currentLine;

            try
            {
                while ((currentLine = reader.readLine()) != null)
                {
                    String[] splitLine = currentLine.split(":");
                    if (splitLine[0].equals("teamnum"))
                    {
                        teamNumTxt.setText(splitLine[1]);
                    }

                    if (splitLine[0].equals("roundnum"))
                    {
                        roundNumber.setText(splitLine[1]);
                    }

                    if (splitLine[0].equals("mobile"))
                    {
                        if (splitLine[1].equals("1"))
                        {
                            mobile.setSelected(true);
                        } else
                        {
                            mobile.setSelected(false);
                        }
                    }

                    if (splitLine[0].equals("noshow"))
                    {
                        if (splitLine[1].equals("1"))
                        {
                            noShow.setSelected(true);
                        } else
                        {
                            noShow.setSelected(false);
                        }
                    }

                    if (splitLine[0].equals("totesauto"))
                    {
                        toteScored.setText(splitLine[1]);
                    }

                    if (splitLine[0].equals("binsauto"))
                    {
                        binStacked.setText(splitLine[1]);
                    }

                    if (splitLine[0].equals("noodauto"))
                    {
                        noodleInserted.setText(splitLine[1]);
                    }

                    if (splitLine[0].equals("ismoving"))
                    {
                        if (splitLine[1].equals("1"))
                        {
                            isMoving.setSelected(true);
                        } else
                        {
                            isMoving.setSelected(false);
                        }
                    }

                    if (splitLine[0].equals("teletotes"))
                    {
                        telTotes.setText(splitLine[1]);
                    }

                    if (splitLine[0].equals("telebins"))
                    {
                        telBins.setText(splitLine[1]);
                    }

                    if (splitLine[0].equals("telenood"))
                    {
                        telNoodles.setText(splitLine[1]);
                    }

                    if (splitLine[0].equals("lifttote"))
                    {
                        if (splitLine[1].equals("1"))
                        {
                            liftTotes.setSelected(true);
                        } else
                        {
                            liftTotes.setSelected(false);
                        }
                    }

                    if (splitLine[0].equals("liftbin"))
                    {
                        if (splitLine[1].equals("1"))
                        {
                            liftBins.setSelected(true);
                        } else
                        {
                            liftBins.setSelected(false);
                        }
                    }

                    if (splitLine[0].equals("liftnood"))
                    {
                        if (splitLine[1].equals("1"))
                        {
                            liftNoodles.setSelected(true);
                        } else
                        {
                            liftNoodles.setSelected(false);
                        }
                    }

                    if (splitLine[0].equals("stacking"))
                    {
                        teamwork.setValue(Float.parseFloat(splitLine[1]));
                    }

                    if (splitLine[0].equals("security"))
                    {
                        security.setValue(Float.parseFloat(splitLine[1]));
                    }

                    if (splitLine[0].equals("durability"))
                    {
                        durability.setValue(Float.parseFloat(splitLine[1]));
                    }
                }
                reader.close();
                System.out.println("Imported");
            } catch (IOException ex)
            {
                Logger.getLogger(Scout.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Scout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void export()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new File(teamNumTxt.getCharacters().toString() + ".sdb"));
            writer.println("teamnum:" + teamNumTxt.getCharacters().toString());

            if (mobile.isSelected())
            {
                writer.println("mobile:1");
            } else
            {
                writer.println("mobile:0");
            }

            if (noShow.isSelected())
            {
                writer.println("noshow:1");
            } else
            {
                writer.println("noshow:0");
            }

            writer.println("totesauto:" + toteScored.getCharacters().toString());
            writer.println("binsauto:" + binStacked.getCharacters().toString());
            writer.println("noodauto:" + noodleInserted.getCharacters().toString());

            if (isMoving.isSelected())
            {
                writer.println("ismoving:1");
            } else
            {
                writer.println("ismoving:0");
            }

            writer.println("teletotes:" + telTotes.getCharacters().toString());
            writer.println("telebins:" + telBins.getCharacters().toString());
            writer.println("telenood:" + telNoodles.getCharacters().toString());

            if (liftTotes.isSelected())
            {
                writer.println("lifttote:1");
            } else
            {
                writer.println("lifttote:0");
            }

            if (liftBins.isSelected())
            {
                writer.println("liftbin:1");
            } else
            {
                writer.println("liftbin:0");
            }

            if (liftNoodles.isSelected())
            {
                writer.println("liftnood:1");
            } else
            {
                writer.println("liftnood:0");
            }
            writer.println("stacking:" + teamwork.getValue());
            writer.println("security:" + security.getValue());
            writer.println("durability:" + durability.getValue());

            System.out.println("Exported");

            writer.close();
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Scout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}
