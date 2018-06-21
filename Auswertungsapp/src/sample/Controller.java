package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import sun.util.resources.en.TimeZoneNames_en_IE;
//import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    @FXML
    private Button readCSV;
    @FXML
    private TextField medianElemente;

    @FXML
    private void leseCSV() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CSV wählen");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV Dateien (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        Reader in = new FileReader(file.getAbsolutePath());


        Iterable<CSVRecord> rec = CSVFormat.RFC4180.withHeader("ID", "Messung_Start", "Android_ID", "zeit", "X", "Y", "Z").parse(in);
        fileChooser = new FileChooser();
        fileChooser.setTitle("CSV wählen");
        fileChooser.getExtensionFilters().add(extFilter);
        file = fileChooser.showSaveDialog(null);


        LinkedList <Double> X = new LinkedList<Double>();
        LinkedList <Double> Y = new LinkedList<Double>();
        LinkedList <Double> Z = new LinkedList<Double>();
        LinkedList <Double> Xbearbeitet = new LinkedList<Double>();
        LinkedList <Double> Ybearbeitet = new LinkedList<Double>();
        LinkedList <Double> Zbearbeitet = new LinkedList<Double>();
        LinkedList <String> ID = new LinkedList<String>();
        LinkedList <String> Messung_Start = new LinkedList<String>();
        LinkedList <String> Android_ID = new LinkedList<String>();
        LinkedList <String> zeit = new LinkedList<String>();

        for (CSVRecord record : rec) {
            if (((CSVParser) rec).getCurrentLineNumber() > 1) {
                X.add(Double.parseDouble(record.get("X")));
                Y.add(Double.parseDouble(record.get("Y")));
                Z.add(Double.parseDouble(record.get("Z")));
                ID.add((record.get("ID")));
                Messung_Start.add(record.get("Messung_Start"));
                Android_ID.add(record.get("Android_ID"));
                zeit.add(record.get("zeit"));
            }
        }

        X = median(X, Integer.parseInt(medianElemente.getText()));
        Y = median(Y, Integer.parseInt(medianElemente.getText()));
        Z = median(Z, Integer.parseInt(medianElemente.getText()));
        /*
        X = ableitung(X, Integer.parseInt(medianElemente.getText()));
        Y = ableitung(Y, Integer.parseInt(medianElemente.getText()));
        Z = ableitung(Z, Integer.parseInt(medianElemente.getText()));
        */
        Xbearbeitet = durchschnitt(X, Integer.parseInt(medianElemente.getText()));
        Ybearbeitet = durchschnitt(Y, Integer.parseInt(medianElemente.getText()));
        Zbearbeitet = durchschnitt(Z, Integer.parseInt(medianElemente.getText()));

        FileWriter out = new FileWriter(file.getAbsolutePath());
        CSVPrinter printer = CSVFormat.DEFAULT
                .withHeader("ID", "Messung_Start", "Android_ID", "zeit", "X", "Y", "Z", "XDurchschnitt", "YDurchschnitt", "ZDurchschnitt").print(out);
        for (int i = 0; i < X.size(); i++) {
            printer.printRecord(ID.get(i), Messung_Start.get(i), Android_ID.get(i), zeit.get(i), X.get(i), Y.get(i), Z.get(i), Xbearbeitet.get(i), Ybearbeitet.get(i), Zbearbeitet.get(i));
        }
        printer.flush();
        System.out.println("Fertig");
        }
    private LinkedList<Double> median(LinkedList <Double> input, int elemente)
    {
        LinkedList<Double> tmp = new LinkedList<Double>();
        Double tmpArray[] = new Double[elemente];
        int mitte = elemente/2;
        //System.out.println(elemente/2);
        for (int i = 0; i < mitte; i++)
        {
            tmp.add(input.get(i));
        }
        for (int i = mitte; i < input.size()-mitte; i++)
        {
            int y = 0;
            for (int z = i-mitte; z < i+mitte; z++) {
                tmpArray[y] = input.get(z);
                y++;
            }
            Arrays.sort(tmpArray);
            tmp.add(tmpArray[elemente/2]);
            //System.out.println("Alter Wert: " + input.get(i-mitte));
            //System.out.println("Neuer Wert: " + tmp.get(i-mitte));
        }
        for (int i = input.size()-mitte; i < input.size(); i++)
        {
            tmp.add(input.get(i));
        }
        System.out.println("Anzahl alter Datensätze: " + input.size());
        System.out.println("Anzahl alter Datensätze: " + tmp.size());
        return tmp;

    }
    private LinkedList<Double> durchschnitt(LinkedList <Double> input, int elemente)
    {
        LinkedList<Double> tmp = new LinkedList<Double>();
        Double tmpArray[] = new Double[elemente];
        int mitte = elemente/2;
        Double tmpWert = 0d;
        //System.out.println(elemente/2);
        for (int i = 0; i < mitte; i++)
        {
            tmp.add(input.get(i));
        }
        for (int i = mitte; i < input.size()-mitte; i++)
        {
            tmpWert = 0d;
            int y = 0;
            for (int z = i-mitte; z < i+mitte; z++) {
                tmpWert += input.get(z);
                y++;
            }
            tmpWert = tmpWert/elemente;
            tmp.add(tmpWert);
            //System.out.println("Alter Wert: " + input.get(i-mitte));
            //System.out.println("Neuer Wert: " + tmp.get(i-mitte));
        }
        for (int i = input.size()-mitte; i < input.size(); i++)
        {
            tmp.add(input.get(i));
        }
        System.out.println("Anzahl alter Datensätze: " + input.size());
        System.out.println("Anzahl alter Datensätze: " + tmp.size());
        return tmp;

    }
    private LinkedList<Double> ableitung(LinkedList <Double> input, int elemente) {
        LinkedList<Double> tmp = new LinkedList<Double>();
        Double tmpArray[] = new Double[elemente];
        Double tmpWert = 0d;

        for (int i = 0; i < input.size() - elemente; i++) {
            if ((input.get(i)-input.get(i + elemente)) > 0.005d ) {
                tmp.add(1d);
            }
            else if ((input.get(i)-input.get(i + elemente)) < -0.005d) {
                tmp.add(-1d);
            }
            else if ((input.get(i)-input.get(i + elemente) >= -0.005d) && ((input.get(i)-input.get(i + elemente) <= 0.005d))) {
                tmp.add(0d);
            }
        //Arrays.sort(tmpArray);
        }
        for (int i = input.size() - elemente; i < input.size(); i++) {
            tmp.add(0d);
        }
            //System.out.println("Alter Wert: " + input.get(i-mitte));
            //System.out.println("Neuer Wert: " + tmp.get(i-mitte));
        System.out.println("Anzahl alter Datensätze: " + input.size());
        System.out.println("Anzahl neue Datensätze: " + tmp.size());
        return tmp;
    }
}
