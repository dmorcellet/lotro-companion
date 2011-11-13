package delta.games.lotro.characterLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import delta.common.utils.text.TextTools;
import delta.common.utils.text.TextUtils;
import delta.games.lotro.characterLog.LotroLogItem.LogItemType;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for LOTRO character log HTML pages. 
 * @author DAM
 */
public class LotroActivityLogPageParser
{
  private static final Logger _logger=LotroLoggers.getCharacterLogLogger();

  private static final String TABLE_START="<table class=\"gradient_table activitylog\">";
  private static final String DATE_ROW_START="<td class=\"date\">";
  private static final String DETAILS_ROW_START="<td class=\"details\">";

  /**
   * Parse an HTML page.
   * @param page Page to parse.
   * @return A list of LOTRO log items, or <code>null</code> if a problem occured.
   */
  public List<LotroLogItem> parseLogPage(File page)
  {
    List<LotroLogItem> ret=null;
    List<String> lines=TextUtils.readAsLines(page);
    int tableStartIndex=-1;
    int index=0;
    for(String line : lines)
    {
      if (line.trim().startsWith(TABLE_START))
      {
        tableStartIndex=index;
        break;
      }
      index++;
    }
    if (tableStartIndex!=-1)
    {
      ret=parseLogItems(lines, tableStartIndex);
    }
    return ret;
  }

  private List<LotroLogItem> parseLogItems(List<String> lines, int startIndex)
  {
    List<LotroLogItem> ret=new ArrayList<LotroLogItem>();
    int nbLines=lines.size();
    List<Integer> startRowIndexes=new ArrayList<Integer>();
    for(int index=startIndex;index<nbLines;index++)
    {
      String line=lines.get(index);
      if (line.trim().startsWith("<tr>"))
      {
        startRowIndexes.add(Integer.valueOf(index));
      }
    }
    if (startRowIndexes.size()>0)
    {
      startRowIndexes.remove(0);
    }
    //System.out.println(startRowIndexes);
    for(Integer startRowIndex : startRowIndexes)
    {
      LotroLogItem logItem=parseLogItem(lines,startRowIndex.intValue());
      if (logItem!=null)
      {
        ret.add(logItem);
      }
    }
    return ret;
  }
  
  private LotroLogItem parseLogItem(List<String> lines, int startRowIndex) {
    String dateStr=null,label=null,url=null;
    LogItemType type=null;
    int nbLines=lines.size();
    for(int index=startRowIndex;index<nbLines;index++)
    {
      String line=lines.get(index).trim();
      if (line.startsWith(DATE_ROW_START))
      {
        dateStr=TextTools.findBetween(line,DATE_ROW_START,"</td>");
      }
      else if (line.startsWith(DETAILS_ROW_START))
      {
        for(int index2=index;index2<nbLines;index2++,index++)
        {
          line=lines.get(index2).trim();
          if (line.contains("images/icons/log/icon_"))
          {
            url=TextTools.findBetween(line,"<a href=\"","\">");
            String imgSrc=TextTools.findBetween(line,"<img src=\"","\" />");
            type=findType(imgSrc);
            label=TextTools.findAfter(line,".png\" />");
            if ((label!=null) && (label.endsWith("</a>")))
            {
              label=label.substring(0,label.length()-4);
            }
            break;
          }
        }
        break;
      }
    }
    LotroLogItem ret=null;
    if ((dateStr!=null) && (type!=null) && (label!=null))
    {
      try
      {
        Calendar c=GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        String[] items=dateStr.split("/");
        int year=Integer.parseInt(items[0]);
        int month=Integer.parseInt(items[1]);
        int day=Integer.parseInt(items[2]);
        label=label.trim();
        if (url!=null)
        {
          url=url.trim();
        }
        c.setTimeInMillis(0);
        c.set(year,month-1,day);
        long date=c.getTimeInMillis();
        ret=new LotroLogItem(date,type,label,url);
      }
      catch(Exception e)
      {
        _logger.error("Cannot parse LOTRO character log item!",e);
      }
    }
    return ret;
  }

  private LogItemType findType(String imgSrc)
  {
    LogItemType type=LogItemType.UNKNOWN;
    if (imgSrc!=null)
    {
      if (imgSrc.endsWith("icon_quest.png"))
      {
        type=LogItemType.QUEST;
      }
      else if (imgSrc.endsWith("icon_profession.png"))
      {
        type=LogItemType.PROFESSION;
      }
      else if (imgSrc.endsWith("icon_levelup.png"))
      {
        type=LogItemType.LEVELUP;
      }
      else if (imgSrc.endsWith("icon_deed.png"))
      {
        type=LogItemType.DEED;
      }
    }
    return type;
  }

  private static List<File> sortFiles(File[] files)
  {
    List<File> ret=new ArrayList<File>();
    if ((files!=null) && (files.length>0))
    {
      for(File file : files)
      {
        if (file.getName().endsWith(".html"))
        {
          ret.add(file);
        }
      }

      Comparator<File> c=new Comparator<File>()
      {
        public int compare(File f1, File f2)
        {
          String name1=f1.getName();
          int n1=Integer.parseInt(name1.substring(0,name1.length()-5));
          String name2=f2.getName();
          int n2=Integer.parseInt(name2.substring(0,name2.length()-5));
          if (n1>n2) return 1;
          if (n1<n2) return -1;
          return 0;
        }
      };
      Collections.sort(ret,c);
    }
    return ret;
  }

  public static void main(String[] args)
  {
    File rootDir=new File("/home/dm/lotroPages/glumlug");
    File[] files=rootDir.listFiles();
    if (files!=null)
    {
      List<File> filesToParse=sortFiles(files);
      List<LotroLogItem> completeLog=new ArrayList<LotroLogItem>();
      LotroActivityLogPageParser parser=new LotroActivityLogPageParser();
      for(File file : filesToParse)
      {
        List<LotroLogItem> items=parser.parseLogPage(file);
        if ((items!=null) && (items.size()>0))
        {
          completeLog.addAll(items);
          /*
          if (items.size()!=20)
          {
            System.out.println(items.size()+" "+file);
          }
          */
        }
      }
      for(LotroLogItem logItem : completeLog)
      {
        System.out.println(logItem);
      }
      System.out.println(completeLog.size());
    }
  }
}
