package command;

import editor.Editor;
import parser.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ScriptCommand extends Command {
  /**
   * Run a sequence of commands from a file.
   *
   * File is specified as the second command word.
   *
   * @param editor The editor instance
   */
  @Override
  public void execute(Editor editor) {
    if (!this.hasWord(2)) {
      // if there is no second word, we don't know what to open...
      editor.print("whichScript");
      return;
    }

    String scriptName = this.getWord(2);
    Parser scriptParser = new Parser();

    try (FileInputStream inputStream = new FileInputStream(scriptName)) {
      scriptParser.setInputStream(inputStream);
      while (!editor.getFinished()) {
        try {
          ArrayList<String> commandWords = scriptParser.getCommand(true);
          editor.processCommand(commandWords);
        } catch (Exception ex) {
          return;
        }
      }
      // Reset the internal state of execution - the _script_ has finished, not the program
      editor.setFinished(false);
    } catch (FileNotFoundException ex) {
      editor.print("cannotFind", scriptName);
    } catch (IOException ex) {
      throw new RuntimeException(editor.getI18nMap().get("panic"));
    }
  }
}
