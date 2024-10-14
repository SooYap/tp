package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_NUMBER;

import java.util.List;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentNumber;

/**
 * Deletes a student from a group
 */
public class DeleteStudentFromGroupCommand extends Command {
    public static final String COMMAND_WORD = "delete_student_grp";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the student identified from the target group by the student number used.\n"
            + "Parameter: GROUP_NAME (must only contain alphanumeric characters and spaces, "
            + "and it should not be blank)\n"
            + "Parameter: STUDENT_NUMBER (must be a string starting with 'A0', followed by 6 numeric figures, "
            + "and ends with a capital letter')\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUP_NAME + "Team 5" + " "
            + PREFIX_STUDENT_NUMBER + " " + "A0123456B";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Student: %1$s from %2$s";

    private final StudentNumber targetStudentNo;
    private final GroupName targetGroupName;

    /**
     * Creates an DeleteStudentFromGroupCommand to delete the specified student {@code sno} from group {@code groupName}
     */
    public DeleteStudentFromGroupCommand(GroupName groupName, StudentNumber sno) {
        this.targetGroupName = groupName;
        this.targetStudentNo = sno;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredPersonList();
        boolean hasFoundTargetStudent = false;
        Student studentToBeDeleted = null;
        if (!model.containsGroupName(targetGroupName)) {
            throw new CommandException(Messages.MESSAGE_GROUP_NAME_NOT_FOUND);
        }
        Group targetGroup = model.findGroup(targetGroupName);
        Set<Student> students = targetGroup.getStudents();
        for (Student stu : students) {
            if (stu.getStudentNumber().equals(targetStudentNo)) {
                hasFoundTargetStudent = true;
                studentToBeDeleted = stu;
                break;
            }
        }
        if (!hasFoundTargetStudent) {
            throw new CommandException(Messages.MESSAGE_STUDENT_NO_NOT_FOUND);
        }

        model.deleteStudentFromGroup(targetGroup, studentToBeDeleted);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(studentToBeDeleted), targetGroupName));
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

        DeleteStudentFromGroupCommand otherDeleteStudentCommand = (DeleteStudentFromGroupCommand) other;
        return targetStudentNo.equals(otherDeleteStudentCommand.targetStudentNo)
                && targetGroupName.equals(otherDeleteStudentCommand.targetGroupName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetGroupName", targetGroupName)
                .add("targetStudentNumber", targetStudentNo)
                .toString();
    }
}