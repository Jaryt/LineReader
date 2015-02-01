package lineCalc;

import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
/**
 * @author Jaryt Bustard 
 */
public class LineCalculator extends JFrame
{

	private JPanel contentPane;

	private JDesktopPane desktopPane;

	private JButton browse;

	private JTextField location, fileTypes;

	private JLabel lines, chars, words, packages, braces, semicolons, imports;

	private int linez, charz, wordz, packagez, openingBracez, closingBracez, semicolonz, spacez, importz;

	public static void main(String[] args)
	{
		new LineCalculator();
	}

	public LineCalculator()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 618, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		desktopPane = new JDesktopPane();
		desktopPane.setBackground(SystemColor.menu);
		contentPane.add(desktopPane, BorderLayout.CENTER);

		location = new JTextField();
		location.setBounds(10, 11, 278, 20);
		desktopPane.add(location);
		location.setColumns(10);

		JButton calculate = new JButton("Calculate");

		calculate.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				calculate();
			}
			
		});

		calculate.setBounds(496, 10, 89, 23);
		desktopPane.add(calculate);

		browse = new JButton("Browse");

		browse.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();

				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (new File(location.getText()) != null)
				{
					fileChooser.setCurrentDirectory(new File(location.getText()));
				}

				int option = fileChooser.showOpenDialog(contentPane);

				if (option == JFileChooser.OPEN_DIALOG)
				{
					location.setText(fileChooser.getSelectedFile().getPath());
				}
			}
			
		});

		browse.setBounds(397, 10, 89, 23);
		desktopPane.add(browse);

		lines = new JLabel("0 lines.");
		lines.setHorizontalAlignment(SwingConstants.CENTER);
		lines.setBounds(10, 37, 278, 14);
		desktopPane.add(lines);

		chars = new JLabel("0 characters, 0 spaces.");
		chars.setHorizontalAlignment(SwingConstants.CENTER);
		chars.setBounds(10, 62, 278, 14);
		desktopPane.add(chars);

		words = new JLabel("0 words.");
		words.setHorizontalAlignment(SwingConstants.CENTER);
		words.setBounds(10, 87, 278, 14);
		desktopPane.add(words);

		packages = new JLabel("0 packages.");
		packages.setHorizontalAlignment(SwingConstants.CENTER);
		packages.setBounds(307, 68, 278, 33);
		desktopPane.add(packages);

		braces = new JLabel("0 braces.");
		braces.setHorizontalAlignment(SwingConstants.CENTER);
		braces.setBounds(307, 44, 278, 48);
		desktopPane.add(braces);

		semicolons = new JLabel("0 semicolons.");
		semicolons.setHorizontalAlignment(SwingConstants.CENTER);
		semicolons.setBounds(307, 43, 278, 14);
		desktopPane.add(semicolons);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(298, 42, 7, 70);
		desktopPane.add(separator);

		fileTypes = new JTextField();
		fileTypes.setText(".txt, .java, .cs");
		fileTypes.setColumns(10);
		fileTypes.setBounds(298, 11, 89, 20);
		desktopPane.add(fileTypes);
		
		imports = new JLabel("0 imports.");
		imports.setHorizontalAlignment(SwingConstants.CENTER);
		imports.setBounds(307, 91, 278, 20);
		desktopPane.add(imports);

		setResizable(false);
		setVisible(true);
	}

	public void calculate()
	{
		File file = new File(location.getText());

		linez = charz = wordz = packagez = openingBracez = closingBracez = semicolonz = spacez = 0;

		if (file != null)
		{
			searchPackage(file);
		}

		lines.setText(linez + " lines.");
		chars.setText(charz + " characters, " + spacez / 2 + " spaces.");
		words.setText(wordz / 2 + " words.");
		packages.setText(packagez / 2 + " packages.");
		braces.setText(openingBracez / 2 + " opening braces, " + closingBracez / 2 + " closing braces.");
		semicolons.setText(semicolonz / 2 + " semicolons.");
		imports.setText(importz + " imports.");

		revalidate();
		repaint();
	}

	public void searchPackage(File file)
	{
		packagez++;

		for (File filez : file.listFiles())
		{
			if (filez.isDirectory())
			{
				searchPackage(filez);
			}
			else if (filez.isFile() && checkType(filez.getName()))
			{
				searchFile(filez);
			}
		}
	}

	public void searchFile(File file)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line = null;

			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("import") || line.startsWith("using"))
				{
					importz++;
				}
				
				linez++;
				charz += line.replace(" ", "").toCharArray().length;
				wordz += line.split(" ").length;
				spacez += line.split(" ").length;
				openingBracez += line.split("\\{").length;
				closingBracez += line.split("}").length;
				semicolonz += line.split(";").length;
			}

			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean checkType(String fileName)
	{
		String types = fileTypes.getText().replace(" ", "");
		String type = fileName.substring(fileName.lastIndexOf("."));

		for (String checkType : types.split(","))
		{
			if (checkType.equals(type))
			{
				return true;
			}
		}

		return false;
	}

}
