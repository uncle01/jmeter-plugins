package com.googlecode.jmeter.plugins.webdriver.sampler.gui;

import com.googlecode.jmeter.plugins.webdriver.sampler.WebDriverSampler;
import jsyntaxpane.DefaultSyntaxKit;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JSR223BeanInfoSupport;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebDriverSamplerGui extends AbstractSamplerGui {

    private static final long serialVersionUID = 100L;
    private static final Logger LOGGER = LoggingManager.getLoggerForClass();

    static {
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            DefaultSyntaxKit.initKit();
        } else {
            LOGGER.info("Headless environment detected. Disabling JSyntaxPane highlighting.");
        }
    }

    JTextField parameters;

    JEditorPane script;
    JComboBox<String> languages;

    public WebDriverSamplerGui() {
        createGui();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("WebDriver Sampler");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        script.setText(element.getPropertyAsString(WebDriverSampler.SCRIPT));
        parameters.setText(element.getPropertyAsString(WebDriverSampler.PARAMETERS));
        languages.setSelectedItem(element.getPropertyAsString(WebDriverSampler.SCRIPT_LANGUAGE));
        super.configure(element);
    }

    @Override
    public TestElement createTestElement() {
        WebDriverSampler sampler = new WebDriverSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        element.clear();
        this.configureTestElement(element);
        element.setProperty(WebDriverSampler.SCRIPT, script.getText());
        element.setProperty(WebDriverSampler.PARAMETERS, parameters.getText());
        element.setProperty(WebDriverSampler.SCRIPT_LANGUAGE, (String) languages.getSelectedItem());
    }

    @Override
    public void clearGui() {
        super.clearGui();

        parameters.setText(""); //$NON-NLS-1$
        script.setText(WebDriverSampler.defaultScript); //$NON-NLS-1$
        languages.setSelectedItem(WebDriverSampler.DEFAULT_ENGINE);
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        Box box = Box.createVerticalBox();
        box.add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), "WebDriverSampler"));
        box.add(createParameterPanel());
        box.add(createLangPanel());
        add(box, BorderLayout.NORTH);

        JPanel panel = createScriptPanel();
        add(panel, BorderLayout.CENTER);
        // Don't let the input field shrink too much
        add(Box.createVerticalStrut(panel.getPreferredSize().height), BorderLayout.WEST);
    }

    private JPanel createParameterPanel() {
        final JLabel label = new JLabel("Parameters:");

        parameters = new JTextField(10);
        parameters.setName(WebDriverSampler.PARAMETERS);
        label.setLabelFor(parameters);

        final JPanel parameterPanel = new JPanel(new BorderLayout(5, 0));
        parameterPanel.add(label, BorderLayout.WEST);
        parameterPanel.add(parameters, BorderLayout.CENTER);

        return parameterPanel;
    }

    private JPanel createLangPanel() {
        final JLabel label = new JLabel("Script Language:");

        String[][] languageNames = JSR223BeanInfoSupport.LANGUAGE_NAMES;
        String[] langs = new String[languageNames.length];
        for (int n = 0; n < languageNames.length; n++) {
            langs[n] = languageNames[n][0];
        }

        languages = new JComboBox<String>(langs);
        languages.setName(WebDriverSampler.PARAMETERS);
        label.setLabelFor(languages);
        languages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JComboBox<String> source = (JComboBox<String>) actionEvent.getSource();
                String ctype = "text/" + source.getSelectedItem();
                setScriptContentType(ctype);
            }
        });

        final JPanel parameterPanel = new JPanel(new BorderLayout(5, 0));
        parameterPanel.add(label, BorderLayout.WEST);
        parameterPanel.add(languages, BorderLayout.CENTER);

        return parameterPanel;
    }

    private void setScriptContentType(String ctype) {
        String text = script.getText();
        script.setContentType(ctype);
        script.setText(text);
    }

    private JPanel createScriptPanel() {
        script = new JEditorPane();
        final JScrollPane scrollPane = new JScrollPane(script);
        setScriptContentType("text/plain");
        script.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        final JLabel label = new JLabel("Script (see below for variables that are defined)");
        label.setLabelFor(script);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        final JTextArea explain = new JTextArea("The following variables are defined for the script: WDS.name, WDS.parameters, WDS.args, WDS.log, WDS.browser, WDS.sampleResult");
        explain.setLineWrap(true);
        explain.setEditable(false);
        explain.setBackground(this.getBackground());
        panel.add(explain, BorderLayout.SOUTH);

        return panel;
    }
}
