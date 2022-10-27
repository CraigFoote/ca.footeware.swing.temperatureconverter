/**
 * 
 */
package ca.footeware.swing.temperatureconverter;

import java.awt.EventQueue;
import java.awt.Font;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * @author Footeware.ca
 */
public class TemperatureConverter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel celsiusLabel;
	private JLabel fahrenheitLabel;
	private JTextField celsiusText;
	private JTextField fahrenheitText;
	private NumberFilter filter;
	private DocumentListener docListener;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new TemperatureConverter().setVisible(true));
	}

	public TemperatureConverter() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Temperature Converter");
		setSize(800, 150);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		getContentPane().add(panel);

		celsiusLabel = new JLabel("Celsius");
		panel.add(celsiusLabel);

		filter = new NumberFilter();
		docListener = new UpdatingListener();

		celsiusText = new JTextField();
		((PlainDocument) celsiusText.getDocument()).setDocumentFilter(filter);
		celsiusText.getDocument().addDocumentListener(docListener);
		panel.add(celsiusText);

		fahrenheitLabel = new JLabel("Fahrenheit");
		panel.add(fahrenheitLabel);

		fahrenheitText = new JTextField();
		((PlainDocument) fahrenheitText.getDocument()).setDocumentFilter(filter);
		fahrenheitText.getDocument().addDocumentListener(docListener);
		panel.add(fahrenheitText);

		Font font = celsiusLabel.getFont().deriveFont(36.0f);
		celsiusLabel.setFont(font);
		celsiusText.setFont(font);
		fahrenheitLabel.setFont(font);
		fahrenheitText.setFont(font);

		layout.putConstraint(SpringLayout.EAST, celsiusLabel, 0, SpringLayout.EAST, fahrenheitLabel);
		layout.putConstraint(SpringLayout.NORTH, celsiusLabel, 5, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, celsiusText, 5, SpringLayout.EAST, fahrenheitLabel);
		layout.putConstraint(SpringLayout.NORTH, celsiusText, 5, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.EAST, celsiusText, -5, SpringLayout.EAST, panel);

		layout.putConstraint(SpringLayout.WEST, fahrenheitLabel, 5, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, fahrenheitLabel, 50, SpringLayout.NORTH, celsiusLabel);
		layout.putConstraint(SpringLayout.WEST, fahrenheitText, 5, SpringLayout.EAST, fahrenheitLabel);
		layout.putConstraint(SpringLayout.NORTH, fahrenheitText, 50, SpringLayout.NORTH, celsiusLabel);
		layout.putConstraint(SpringLayout.EAST, fahrenheitText, -5, SpringLayout.EAST, panel);
	}

	protected String calculateFahrenheit() {
		String celsiusStr = celsiusText.getText().trim();
		try {
			double celsius = Double.parseDouble(celsiusStr);
			return String.valueOf((celsius * 9 / 5) + 32);
		} catch (NumberFormatException e) {
			return "";
		}
	}

	protected String calculateCelsius() {
		String fahrenheitStr = fahrenheitText.getText().trim();
		try {
			double fahrenheit = Double.parseDouble(fahrenheitStr);
			return String.valueOf(((fahrenheit - 32) * 5) / 9);
		} catch (NumberFormatException e) {
			return "";
		}
	}

	private class UpdatingListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			throw new UnsupportedOperationException();
		}

		public void removeUpdate(DocumentEvent e) {
			update();
		}

		public void insertUpdate(DocumentEvent e) {
			update();
		}
		
		private void update() {
			if (celsiusText.isFocusOwner()) {
				String fahrenheit = calculateFahrenheit();
				fahrenheitText.getDocument().removeDocumentListener(docListener);
				fahrenheitText.setText(fahrenheit);
				fahrenheitText.getDocument().addDocumentListener(docListener);
			} else {
				String celsius = calculateCelsius();
				celsiusText.getDocument().removeDocumentListener(docListener);
				celsiusText.setText(celsius);
				celsiusText.getDocument().addDocumentListener(docListener);
			}
		}
	}

	private class NumberFilter extends DocumentFilter implements Serializable {
		private static final long serialVersionUID = 1L;
		// remove all but 0 to 9, dash, dot, and e or E.
		private static final String REMOVE_REGEX = "[^0-9\\-\\.eE]";

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
			text = text.replaceAll(REMOVE_REGEX, "");
			super.replace(fb, offset, length, text, attrs);
		}
	}
}
