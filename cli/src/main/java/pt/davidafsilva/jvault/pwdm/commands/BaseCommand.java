package pt.davidafsilva.jvault.pwdm.commands;

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

/**
 * The base command implementation
 *
 * @author David Silva
 */
abstract class BaseCommand implements Command {

  // the configured jCommander
  private final JCommander commander;

  /**
   * The default constructor, which receives the commander (being) configured.
   *
   * @param commander the commander
   */
  BaseCommand(final JCommander commander) {
    this.commander = commander;
  }

  /**
   * Returns the commander (being) configured
   *
   * @return the commander
   */
  JCommander getCommander() {
    return commander;
  }

  /**
   * Formats Prints the specified message
   *
   * @param message the message
   * @param args    the arguments to be included in the formatted message
   */
  void print(final String message, final String... args) {
    JCommander.getConsole().print(String.format(message, args));
  }

  /**
   * Formats Prints the specified message
   *
   * @param message the message
   * @param args    the arguments to be included in the formatted message
   */
  void println(final String message, final String... args) {
    JCommander.getConsole().println(String.format(message, args));
  }
}
