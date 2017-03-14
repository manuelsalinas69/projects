package pdi.evaluations.main;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import pdi.evaluations.test.TestLauncher;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SerialException, SQLException {
		TestLauncher launcher=new TestLauncher();
		launcher.launch();
	
	}

	

	
}
