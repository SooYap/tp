package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_NUMBER;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentNumber;

import java.util.List;

public class DeleteStudentCommand extends Command{
    public static final String COMMAND_WORD = "delete_student";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the student identified by the student number used.\n"
            + "Parameters: STUDENT_NUMBER (must be a string starting with 'A', followed by 7 numeric figures, " +
                "and ended with a capital letter')\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STUDENT_NUMBER + " " + "A0123456B";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Student: %1$s";

    private final StudentNumber targetStudentNo;

    public DeleteStudentCommand(StudentNumber sno) {
        this.targetStudentNo = sno;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredPersonList();

        boolean hasFoundTarget = false;
        Student studentToBeDeleted = null;
        for (Student stu : lastShownList) {
            if (stu.getStudentNumber().equals(targetStudentNo)) {
                hasFoundTarget = true;
                studentToBeDeleted = stu;
                break;
            }
        }

        if (!hasFoundTarget) {
            throw new CommandException(Messages.MESSAGE_STUDENT_NO_NOT_FOUND);
        }

        model.deletePerson(studentToBeDeleted);  // Use the correct variable
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(studentToBeDeleted)));  // Use correct variable
    }

    public StudentNumber getTargetStudentNo() {
        return targetStudentNo;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteStudentCommand)) {
            return false;
        }

        DeleteStudentCommand otherDeleteStudentCommand = (DeleteStudentCommand) other;
        return targetStudentNo.equals(otherDeleteStudentCommand.targetStudentNo);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetStudentNumber", targetStudentNo)
                .toString();
    }
}
