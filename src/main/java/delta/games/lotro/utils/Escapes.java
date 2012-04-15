package delta.games.lotro.utils;

/**
 * Escapes management methods.
 * @author DAM
 */
public class Escapes
{
  /**
   * Build a filename from an identifier.
   * @param id Source identifier.
   * @return A filename, including XML extension.
   */
  public static String identifierToFilename(String id)
  {
    String filename=id+".xml";
    filename=filename.replace(":","%3A");
    filename=filename.replace("'","%27");
    filename=filename.replace("â","%C3%A2");
    filename=filename.replace("ä","%C3%A4");
    filename=filename.replace("á","%C3%A1");
    filename=filename.replace("Â","%C3%82");
    filename=filename.replace("Á","%C3%81");
    filename=filename.replace("ë","%C3%AB");
    filename=filename.replace("é","%C3%A9");
    filename=filename.replace("í","%C3%AD");
    filename=filename.replace("î","%C3%AE");
    filename=filename.replace("ó","%C3%B3");
    filename=filename.replace("û","%C3%BB");
    filename=filename.replace("ú","%C3%BA");
    filename=filename.replace("?","%3F");
    
    return filename;
  }

  /**
   * Escape an identifier.
   * @param id Source identifier.
   * @return Escaped string.
   */
  public static String escapeIdentifier(String id)
  {
    id=id.replace(":","%3A");
    id=id.replace("'","%27");
    id=id.replace("â","%C3%A2");
    id=id.replace("ä","%C3%A4");
    id=id.replace("á","%C3%A1");
    id=id.replace("Â","%C3%82");
    id=id.replace("Á","%C3%81");
    id=id.replace("ë","%C3%AB");
    id=id.replace("é","%C3%A9");
    id=id.replace("í","%C3%AD");
    id=id.replace("î","%C3%AE");
    id=id.replace("ó","%C3%B3");
    id=id.replace("û","%C3%BB");
    id=id.replace("ú","%C3%BA");
    id=id.replace("?","%3F");
    id=id.replace(",","%2C");
    
    return id;
  }
}