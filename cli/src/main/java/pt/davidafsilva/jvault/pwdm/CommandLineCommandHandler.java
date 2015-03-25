package pt.davidafsilva.jvault.pwdm;

/*
 * #%L
 * jVault-pwdm
 * %%
 * Copyright (C) 2014 - 2015 David Silva
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the David Silva nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.ParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.davidafsilva.jvault.pwdm.commands.Command;
import pt.davidafsilva.jvault.pwdm.commands.CommandLineOptionsFactory;

/**
 * The command line command handler, which will handle all of the issued commands.
 *
 * @author David Silva
 */
class CommandLineCommandHandler {

  // the logger instance
  private static final Logger log = LoggerFactory.getLogger(CommandLineCommandHandler.class);

  /**
   * Handles the command line arguments accordingly.
   *
   * @param args the command line arguments
   */
  public void handle(final String[] args) {
    // initialize the commander
    final JCommander commander = new JCommander();
    commander.setProgramName("jvault");
    commander.setAcceptUnknownOptions(false);
    commander.setAcceptUnknownOptions(true);

    // create the command configuration
    CommandLineOptionsFactory.create(commander);

    try {
      // parse the args
      commander.parse(args);

      // get the parsed command
      final JCommander command = commander.getCommands().get(commander.getParsedCommand());
      if (command == null) {
        // no command provided
        JCommander.getConsole().println("'jvault help' shows all the commands available." +
                                        " See 'jvault help <command>' for specific help.");
      } else {
        // execute the command
        ((Command) command.getObjects().get(0)).execute();
      }
    } catch (final MissingCommandException e) {
      log.error("Unknown command", e);
      JCommander.getConsole().println("Unknown command: " + e.getMessage().substring(24));
    } catch (final ParameterException e) {
      log.error("Invalid parameter(s)", e);
      JCommander.getConsole().println(e.getMessage());
    } catch (final Exception e) {
      log.error("Command execution error", e);
      JCommander.getConsole().println("Error: " + e.getMessage());
    }
  }
}
