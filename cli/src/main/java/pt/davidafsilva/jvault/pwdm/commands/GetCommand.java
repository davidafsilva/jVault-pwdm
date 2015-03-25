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
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pt.davidafsilva.jvault.model.SecureEntry;
import pt.davidafsilva.jvault.vault.VaultInitializationException;
import pt.davidafsilva.jvault.vault.VaultOperationException;

/**
 * The <stong>get</stong> command line option operation configuration.
 *
 * @author David Silva
 */
@Parameters(
    commandNames = {"get"},
    commandDescription = "search for a secret saved in the vault with the associated key"
)
class GetCommand extends VaultCommand {

  @Parameter(
      description = "<key>",
      arity = 1,
      required = true
  )
  private List<String> keys = new ArrayList<>();

  /**
   * The default constructor, which receives the commander (being) configured.
   *
   * @param commander the commander
   */
  GetCommand(final JCommander commander) {
    super(commander);
  }

  @Override
  public void execute() throws CommandExecutionException {
    if (keys.size() != 1) {
      throw new CommandExecutionException("Expected exactly one key to search the vault");
    }

    // get the key
    final String key = keys.get(0);

    try {
      // read the entry from the vault
      final Optional<SecureEntry> entry = getVault().read(key);
      if (!entry.isPresent()) {
        println("unable to find vault entry for key: %s", key);
      } else {
        println("%s", getVault().translate(entry.get()).getValue());
      }
    } catch (final VaultInitializationException | IOException | VaultOperationException e) {
      throw new CommandExecutionException(e.getMessage(), e);
    }
  }
}
