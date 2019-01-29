/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    SetLastExecutionServlet.java
 *    Copyright (C) 2011-2013 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Used by slaves to pass back execution date-time information for tasks to
 * their master.
 * 
 * @author Mark Hall (mhall{[at]}pentaho{[dot]}com)
 * @version $Revision$
 */
public class SetLastExecutionServlet extends WekaServlet {

  /** The context path for this servlet */
  public static final String CONTEXT_PATH = "/weka/setLastExecution";

  /** Date formatting string to use */
  protected static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

  /**
   * For serialization
   */
  private static final long serialVersionUID = 3158750948290757404L;

  /**
   * Constructs a new SetLastExecutionServlet
   * 
   * @param taskMap the task map maintained by the server
   * @param server a reference to the server itself
   */
  public SetLastExecutionServlet(WekaTaskMap taskMap, WekaServer server) {
    super(taskMap, server);
  }

  /**
   * Process a HTTP GET
   * 
   * @param request the request
   * @param response the response
   * 
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    if (!request.getRequestURI().startsWith(CONTEXT_PATH)) {
      return;
    }

    // this should never be anything but a "client" servlet

    // IMPORTANT this will be the ID as generated by the slave, so it
    // will/should match a getRemoteID() in one of our WekaTaskEntries
    String taskName = request.getParameter("name");

    String lastDate = request.getParameter("lastExecution");

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/octet-stream");

    ObjectOutputStream oos = null;

    try {
      List<WekaTaskMap.WekaTaskEntry> entries = m_taskMap.getTaskList();
      WekaTaskMap.WekaTaskEntry found = null;
      for (WekaTaskMap.WekaTaskEntry te : entries) {
        if (te.getRemoteID() != null && te.getRemoteID().equals(taskName)) {
          found = te;
          break;
        }
      }

      if (found != null) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        if (lastDate != null && lastDate.length() > 0) {
          try {
            Date lastExecution = sdf.parse(lastDate);
            found.setLastExecution(lastExecution);
            NamedTask task = m_taskMap.getTask(found);
            if (task != null) {
              // make sure we persist this information
              m_server.persistTask(found, task);

              // pass on to our master?
              if (found.getCameFromMaster()) {
                m_server.sendExecutionTimeToMaster(found);
              }
            } else {
              System.err.println("[SetLastExecutionServlet]: Task is null ("
                + found.toString() + ")");
            }
          } catch (ParseException e) {
            // ignore
          }

          String okResult = WekaServlet.RESPONSE_OK;
          OutputStream out = response.getOutputStream();
          oos = new ObjectOutputStream(new BufferedOutputStream(out));
          oos.writeObject(okResult);
          oos.flush();
        }

      } else {
        System.err
          .println("[WekaServer] Received a task execution time update from a slave, "
            + "but was unable to find task with remote ID '"
            + taskName
            + "' in the list of tasks registered with this server.");

        String errorResult = WekaServlet.RESPONSE_ERROR
          + ": Master received execution time "
          + "update but couldn't find taks with remote ID '" + taskName
          + "' in its " + "list of registered tasks";
        OutputStream out = response.getOutputStream();
        oos = new ObjectOutputStream(new BufferedOutputStream(out));
        oos.writeObject(errorResult);
        oos.flush();
      }
    } catch (Exception ex) {
      if (oos != null) {
        oos.writeObject(WekaServlet.RESPONSE_ERROR + " " + ex.getMessage());
        oos.flush();
      }
      ex.printStackTrace();
    } finally {
      if (oos != null) {
        oos.close();
      }
    }
  }
}
