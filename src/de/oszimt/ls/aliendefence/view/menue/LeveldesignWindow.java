package de.oszimt.ls.aliendefence.view.menue;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.oszimt.ls.aliendefence.controller.AlienDefenceController;
import de.oszimt.ls.aliendefence.controller.LevelController;
import de.oszimt.ls.aliendefence.model.Level;
import de.oszimt.ls.aliendefence.toDo.User;

@SuppressWarnings("serial")
public class LeveldesignWindow extends JFrame {

	private LevelController lvlControl;
	private JPanel contentPane;
	public LevelChoice levelChoice;
	private LevelEditor cardLevelEditor;

	private CardLayout cards;

	/**
	 * Create the frame.
	 */
	public LeveldesignWindow(AlienDefenceController controller, User user, String menuKnopf) {
		this.lvlControl = controller.getLevelController();

		setTitle("Leveldesigner");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		this.cards = new CardLayout();
		contentPane.setLayout(cards);

		levelChoice = new LevelChoice(controller, this, user, menuKnopf);
		contentPane.add(levelChoice.getPanel(), "levelChooser");

		this.cardLevelEditor = new LevelEditor(this, controller, Level.getDefaultLevel());
		contentPane.add(cardLevelEditor, "levelEditor");

		this.showLevelChooser();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * display leveleditor with a new level
	 */
	public void startLevelEditor() {
		this.cardLevelEditor.setLvl(this.lvlControl.createLevel());
		this.cards.show(contentPane, "levelEditor");
	}

	/**
	 * disply leveleditor with a new level
	 * 
	 * @param level_id
	 */
	public void startLevelEditor(int level_id) {
		this.cardLevelEditor.setLvl(this.lvlControl.readLevel(level_id));
		this.cards.show(contentPane, "levelEditor");
	}

	/**
	 * display a jTable with all Levels
	 */
	public void showLevelChooser() {
		this.cards.show(contentPane, "levelChooser");
		levelChoice.updateTableData();
	}

}
