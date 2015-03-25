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
import com.beust.jcommander.converters.FileConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicReference;

import pt.davidafsilva.jvault.vault.FileVault;
import pt.davidafsilva.jvault.vault.VaultBuilder;
import pt.davidafsilva.jvault.vault.VaultInitializationException;

/**
 * The base vault command, which requires vault data to be executed.
 *
 * @author David Silva
 */
abstract class VaultCommand extends BaseCommand {

  @Parameter(
      names = "--vault-folder",
      description = "Specifies the vault's configuration folder",
      arity = 1,
      required = true,
      converter = FileConverter.class
  )
  private File vaultFolder;

  @Parameter(
      names = "--with-password",
      description = "Specify the password shall be used to operate on the vault",
      arity = 1,
      required = true,
      password = true
  )
  private String vaultPassword;

  // the vault file
  private AtomicReference<FileVault> vaultRef = new AtomicReference<>();

  /**
   * The default constructor, which receives the commander (being) configured.
   *
   * @param commander the commander
   */
  VaultCommand(final JCommander commander) {
    super(commander);
  }

  /**
   * Returns the configured vault instance for the command execution
   *
   * @return the vault instance
   * @throws IOException                  if any error occurs while reading or creating either the
   *                                      salt file or the vault file.
   * @throws VaultInitializationException if the vault cannot be initialized
   */
  FileVault getVault() throws VaultInitializationException, IOException {
    FileVault fileVault = vaultRef.get();
    if (fileVault == null) {
      final byte[] salt = getOrCreateSalt();
      final Path vaultFile = Paths.get(vaultFolder.getPath(), "vault");
      if (!vaultFile.toFile().exists()) {
        Files.createFile(vaultFile);
      }
      fileVault = (FileVault) VaultBuilder.create()
          .rawFile(vaultFile)
          .salt(salt)
          .password(vaultPassword)
          .build();
      if (!vaultRef.compareAndSet(null, fileVault)) {
        return vaultRef.get();
      }
    }

    return fileVault;
  }

  /**
   * Either creates a salt if one does not exist in the configuration folder, otherwise it creates
   * one.
   *
   * @return the salt content
   */
  private byte[] getOrCreateSalt() throws IOException {
    final Path saltFile = Paths.get(vaultFolder.getPath(), "salt");
    final File fp = saltFile.toFile();

    // check if it exists
    if (fp.exists()) {
      if (!fp.isFile() || !fp.canRead()) {
        throw new IOException("Unable to read salt file");
      }
      return Files.readAllBytes(saltFile);
    }

    // create a new one
    final byte[] salt = createSalt();

    // store the salt
    final Path parentDir = saltFile.getParent();
    if (!Files.exists(parentDir)) {
      Files.createDirectories(parentDir);
    }
    Files.write(saltFile, salt);

    return salt;
  }

  /**
   * Creates a random 16 byte salt
   *
   * @return the generated salt
   */
  private byte[] createSalt() {
    final SecureRandom random = new SecureRandom();
    final byte[] salt = new byte[16];
    random.nextBytes(salt);
    return salt;
  }
}
