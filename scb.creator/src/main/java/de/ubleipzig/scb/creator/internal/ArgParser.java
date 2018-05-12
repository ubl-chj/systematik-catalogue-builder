/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.ubleipzig.scb.creator.internal;

import static java.lang.System.out;
import static org.apache.commons.cli.Option.builder;
import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.scb.creator.ResourceCreator;
import de.ubleipzig.scb.creator.ScbConfig;
import de.ubleipzig.scb.creator.SystematikCatalogueBuilder;
import de.ubleipzig.scb.creator.TaggingAnnotationCreator;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;

public class ArgParser {

    private static Logger logger = getLogger(ArgParser.class);
    private static final Options configOptions = new Options();

    static {
        configOptions.addOption(
                builder("b").longOpt("builder").hasArg(true).desc("Builder Type").required(true).build());

        configOptions.addOption(builder("f").longOpt("from").hasArg(true).desc("From Index").required(true).build());

        configOptions.addOption(builder("t").longOpt("to").hasArg(true).desc("To Index").required(true).build());

        configOptions.addOption(
                Option.builder("c").longOpt("config").hasArg(true).numberOfArgs(1).argName("config").desc(
                        "Path to config file").required(true).build());
    }

    /**
     * Parse command-line arguments.
     *
     * @param args Command-line arguments
     * @return A configured SystematikCatalogueBuilder instance.
     **/
    SystematikCatalogueBuilder init(final String[] args) {
        final ScbConfig scbConfig = parseConfiguration(args);
        final String builderType = Objects.requireNonNull(scbConfig).getBuilderType();
        switch (builderType) {
            case "resources":
                return new ResourceCreator(Objects.requireNonNull(scbConfig));
            case "tagging":
                return new TaggingAnnotationCreator(Objects.requireNonNull(scbConfig));
            default:
                return new ResourceCreator(Objects.requireNonNull(scbConfig));
        }
    }

    private ScbConfig parseConfiguration(final String[] args) {
        final CommandLine c;
        ScbConfig config;
        try {
            c = parseConfigArgs(configOptions, args);
            config = parseConfigFileOptions(c);
            config = addSharedOptions(c, config);
            return config;
        } catch (final ParseException ignore) {
            logger.debug("Command line argments weren't valid for specifying a config file.");
        }
        return null;
    }

    /**
     * Parse command line options based on the provide Options.
     *
     * @param args command line arguments
     * @return the list of option and values
     * @throws ParseException if invalid/missing option is found
     */
    private static CommandLine parseConfigArgs(final Options configOptions, final String[] args) throws ParseException {
        return new DefaultParser().parse(configOptions, args);
    }


    private ScbConfig parseConfigFileOptions(final CommandLine cmd) {
        return retrieveConfig(new File(cmd.getOptionValue('c')));
    }

    /**
     * This method parses the provided configFile into its equivalent command-line args.
     *
     * @param configFile containing config args
     * @return Array of args
     */
    protected ScbConfig retrieveConfig(final File configFile) {
        if (!configFile.exists()) {
            printHelp("Configuration file does not exist: " + configFile);
        }
        logger.debug("Loading configuration file: {}", configFile);
        try {
            return new YamlConfigurationFactory<>(
                    ScbConfig.class, Validators.newValidator(), Jackson.newObjectMapper(), "").build(configFile);
        } catch (IOException | ConfigurationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * This method add/updates the values of any options that may be
     * valid in either scenario (config file or fully command line).
     *
     * @param cmd a parsed command line
     * @return Config the config which may be updated
     */
    private ScbConfig addSharedOptions(final CommandLine cmd, final ScbConfig config) {
        final String builderType = cmd.getOptionValue("b");
        final String fromIndex = cmd.getOptionValue("f");
        final String toIndex = cmd.getOptionValue("t");
        config.setBuilderType(builderType);
        config.setFromIndex(Integer.valueOf(fromIndex));
        config.setToIndex(Integer.valueOf(toIndex));
        return config;
    }


    /**
     * Print help/usage information.
     *
     * @param message the message or null for none
     */
    private void printHelp(final String message) {
        final HelpFormatter formatter = new HelpFormatter();
        final PrintWriter writer = new PrintWriter(out);
        writer.println("\n-----------------------\n" + message + "\n-----------------------\n");
        writer.println("Running SCB from command line arguments");
        formatter.printHelp(writer, 80, "./scb", "", configOptions, 4, 4, "", true);
        writer.println("\n");
        writer.flush();
        throw new RuntimeException(message);
    }
}
