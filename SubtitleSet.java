import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class SubtitleSet {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String edited = "";
		int mSecondDelay;
		int secondDelay;
		int minuteDelay;
		int hourDelay;
		System.out.println("enter deley in hour, minute, second, milisecond (example: 0 0 1 500):");
		String[] deley = in.nextLine().split(" ");
		hourDelay = Integer.parseInt(deley[0]);
		minuteDelay = Integer.parseInt(deley[1]);
		secondDelay = Integer.parseInt(deley[2]);
		mSecondDelay = Integer.parseInt(deley[3]);
		in.close();
		File file = new File("./salag.srt");
		String editedFile = "./edition" + file.hashCode() + ".srt";
		try (
			Scanner scanner = new Scanner(file);
			Formatter formatter = new Formatter(editedFile);
		) {
			String line = scanner.nextLine();
			while (scanner.hasNextLine()) {
				edited = line;
				if (line.matches("\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}")) {
					if (mSecondDelay != 0) {
						edited = mSecondEditor(edited, mSecondDelay);
					}
					if (secondDelay != 0) {
						edited = secondEditor(edited, secondDelay);
					}
					if (minuteDelay != 0) {
						edited = minuteEditor(edited, minuteDelay);
					}
					if (hourDelay != 0) {
						edited = hourEditor(edited, hourDelay);
					}
				}
				formatter.format("%s\n", edited);
				System.out.println(edited);
				line = scanner.nextLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String mSecondEditor(String line, int added) {
		String num = "";
		String sub1 = "", sub2 = "";
		boolean secondMS = false;
		int number = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == (',')) {
				for (int j = i + 1; j < i + 4; j++) {
					num += line.charAt(j);
				}
				number = Integer.parseInt(num);
				number += added;
				if (number >= 1000) {
					number -= 1000;
					line = oneSecondEditor(line, secondMS, 1);
				}
				if (number < 0) {
					number += 1000;
					line = oneSecondEditor(line, secondMS, -1);
				}
				if (secondMS) {
					sub1 = line.substring(0, 26);
					line = sub1 + makeStandardMS(number + "");
				} else {
					sub1 = line.substring(0, 9);
					sub2 = line.substring(12);
					line = sub1 + makeStandardMS(number + "") + sub2;
				}
				num = "";
				secondMS = true;
			}
		}
		return line;
	}

	public static String secondEditor(String line, int added) {
		String num = "";
		int number = 0;
		boolean secondS = false;
		int doubleQuotnum = 0;
		String sub1 = "", sub2 = "";
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == (':')) {
				doubleQuotnum++;
				if (doubleQuotnum == 2) {
					for (int j = i + 1; j < i + 3; j++) {
						num += line.charAt(j);
					}
					number = Integer.parseInt(num);
					number += added;
					if (number >= 60) {
						number -= 60;
						line = oneMinuteEditor(line, secondS, 1);
					}
					if (number < 0) {
						number += 60;
						line = oneHourEditor(line, secondS, -1);
					}
					if (secondS) {
						sub1 = line.substring(0, 23);
						sub2 = line.substring(25);
						line = sub1 + makeStandardOther(number + "") + sub2;
					} else {
						sub1 = line.substring(0, 6);
						sub2 = line.substring(8);
						line = sub1 + makeStandardOther(number + "") + sub2;
					}
					num = "";
					secondS = true;
					doubleQuotnum = 0;
				}
			}
		}
		return line;
	}

	public static String minuteEditor(String line, int added) {
		String num = "";
		int number = 0;
		boolean secondM = false;
		int doubleQuotnum = 0;
		String sub1 = "", sub2 = "";
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == (':')) {
				doubleQuotnum++;
				if (doubleQuotnum == (secondM ? 2 : 1)) {
					for (int j = i + 1; j < i + 3; j++) {
						num += line.charAt(j);
					}
					number = Integer.parseInt(num);
					number += added;
					if (number >= 60) {
						number -= 60;
						line = oneHourEditor(line, secondM, 1);
					}
					if (number < 0) {
						number += 60;
						line = oneHourEditor(line, secondM, -1);
					}
					if (secondM) {
						sub1 = line.substring(0, 20);
						sub2 = line.substring(22);
						line = sub1 + makeStandardOther(number + "") + sub2;
					} else {
						sub1 = line.substring(0, 3);
						sub2 = line.substring(5);
						line = sub1 + makeStandardOther(number + "") + sub2;
					}
					num = "";
					secondM = true;
					doubleQuotnum = 0;
				}
			}
		}
		return line;
	}

	public static String hourEditor(String line, int added) {
		String num = "";
		int number = 0;
		boolean secondH = false;
		int doubleQuotnum = 0;
		boolean first = true;
		String sub1 = "", sub2 = "";
		for (int i = 0; i < line.length(); i++) {
			if (i == 0 || i == 16) {
				if (first == false) {
					doubleQuotnum++;
				} else {
					i = -1;
				}
				if (doubleQuotnum == (secondH ? 2 : 0)) {
					for (int j = i + 1; j < i + 3; j++) {
						num += line.charAt(j);
					}
					number = Integer.parseInt(num);
					number += added;
					if (secondH) {
						sub1 = line.substring(0, 17);
						sub2 = line.substring(19);
						line = sub1 + makeStandardOther(number + "") + sub2;
					} else {
						sub1 = line.substring(2);
						line = makeStandardOther(number + "") + sub1;
					}
					num = "";
					secondH = true;
					doubleQuotnum = 0;
					first = false;
				}
			}
		}
		return line;
	}

	public static String makeStandardMS(String input) {
		switch (input.length()) {
			case 1:
				input = "00" + input;
				break;
			case 2:
				input = "0" + input;
				break;
		}
		return input;
	}

	public static String makeStandardOther(String input) {
		if (input.length() == 1) {
			input = "0" + input;
		}
		return input;
	}

	public static String oneSecondEditor(String line, boolean flag, int added) {
		String num = "";
		int number = 0;
		int doubleQuotnum = 0;
		String sub1 = "", sub2 = "";
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == (':')) {
				doubleQuotnum++;
				if (doubleQuotnum == (flag ? 4 : 2)) {
					for (int j = i + 1; j < i + 3; j++) {
						num += line.charAt(j);
					}
					number = Integer.parseInt(num);
					number += added;
					if (number >= 60) {
						number -= 60;
						line = oneMinuteEditor(line, flag, 1);
					}
					if (number < 0) {
						number += 60;
						line = oneMinuteEditor(line, flag, -1);
					}
					if (flag) {
						sub1 = line.substring(0, 23);
						sub2 = line.substring(25);
						line = sub1 + makeStandardOther(number + "") + sub2;
					} else {
						sub1 = line.substring(0, 6);
						sub2 = line.substring(8);
						line = sub1 + makeStandardOther(number + "") + sub2;
					}
					break;
				}
			}
		}
		return line;
	}

	public static String oneMinuteEditor(String line, boolean flag, int added) {
		String num = "";
		int number = 0;
		int doubleQuotnum = 0;
		String sub1 = "", sub2 = "";
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == (':')) {
				doubleQuotnum++;
				if (doubleQuotnum == (flag ? 3 : 1)) {
					for (int j = i + 1; j < i + 3; j++) {
						num += line.charAt(j);
					}
					number = Integer.parseInt(num);
					number += added;
					if (number >= 60) {
						number -= 60;
						oneHourEditor(line, flag, 1);
					}
					if (number < 0) {
						number += 1000;
						line = oneHourEditor(line, flag, -1);
					}
					if (flag) {
						sub1 = line.substring(0, 20);
						sub2 = line.substring(22);
						line = sub1 + makeStandardOther(number + "") + sub2;
					} else {
						sub1 = line.substring(0, 3);
						sub2 = line.substring(5);
						line = sub1 + makeStandardOther(number + "") + sub2;
					}
					break;
				}
			}
		}
		return line;
	}

	public static String oneHourEditor(String line, boolean flag, int added) {
		String num = "";
		int number = 0;
		int doubleQuotnum = 0;
		String sub1 = "", sub2 = "";
		for (int i = 0; i < line.length(); i++) {
			if (i == 0 || i == 16) {
				if (flag == true) {
					doubleQuotnum++;
				} else {
					i = -1;
				}
				if (doubleQuotnum == (flag ? 2 : 0)) {
					for (int j = i + 1; j < i + 3; j++) {
						num += line.charAt(j);
					}
					number = Integer.parseInt(num);
					number += added;
					System.out.println(number);
					if (flag) {
						sub1 = line.substring(0, 17);
						sub2 = line.substring(19);
						line = sub1 + makeStandardOther(number + "") + sub2;
					} else {
						sub1 = line.substring(2);
						line = makeStandardOther(number + "") + sub1;
					}
					break;
				}
			}
		}
		return line;
	}
}
