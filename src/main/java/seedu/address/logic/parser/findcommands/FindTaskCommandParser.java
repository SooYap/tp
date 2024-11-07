package seedu.address.logic.parser.findcommands;

import static seedu.address.logic.Messages.MESSAGE_ILLEGAL_PREFIX_USED;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.ALL_PREFIX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUERY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.findcommands.FindTaskCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.TaskNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindTaskCommand object
 */
public class FindTaskCommandParser implements Parser<FindTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindTaskCommand
     * and returns an FindTaskCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindTaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_QUERY);

        List<Prefix> allowedPrefix = new ArrayList<Prefix>(Arrays.asList(PREFIX_QUERY));
        List<Prefix> invalidPrefixes = ALL_PREFIX;
        invalidPrefixes.removeAll(allowedPrefix);
        if (containsInvalidPrefix(args, invalidPrefixes)) {
            throw new ParseException(MESSAGE_ILLEGAL_PREFIX_USED + "\n" + FindTaskCommand.MESSAGE_USAGE);
        }
        if (!arePrefixesPresent(argMultimap, PREFIX_QUERY) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTaskCommand.MESSAGE_USAGE));
        }

        String[] queryList = argMultimap.getAllValues(PREFIX_QUERY).toArray(new String[0]);

        return new FindTaskCommand(new TaskNameContainsKeywordsPredicate(Arrays.asList(queryList)));
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private boolean containsInvalidPrefix(String arg, List<Prefix> invalidPrefixes) {
        return invalidPrefixes.stream().anyMatch(prefix -> arg.contains(prefix.getPrefix()));
    }
}
