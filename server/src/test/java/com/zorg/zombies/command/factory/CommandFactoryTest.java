package com.zorg.zombies.command.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.ErrorCommand;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.MoveDirectionX;
import com.zorg.zombies.model.factory.MoveDirectionFactory;
import com.zorg.zombies.model.factory.WrongMoveDirectionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private final MoveDirectionFactory moveDirectionFactory = new MoveDirectionFactory();
    private final CommandFactory commandFactory = new CommandFactory(moveDirectionFactory);

    @Test
    void commandParse_When_InvalidMoveDirectionInMoveCommandSent_Expect_WrongMoveDirectionThrown() throws Exception {

        final UserMoveCommand command = new UserMoveCommand(WrongMoveDirection.WRONG);
        final String commandAsJson = mapper.writeValueAsString(command);
        final Command parsed = commandFactory.fromJson(commandAsJson);

        assertTrue(parsed instanceof ErrorCommand);

        final ErrorCommand errorCommand = (ErrorCommand) parsed;

        assertTrue(errorCommand.getError() instanceof WrongMoveDirectionException);
    }

    @Test
    void commandParse_When_ValidMoveCommandSent_Expect_Parsed() throws Exception {

        final UserMoveCommand command = new UserMoveCommand(MoveDirectionX.WEST);
        final String commandAsJson = mapper.writeValueAsString(command);

        final Command parsed = commandFactory.fromJson(commandAsJson);

        assertEquals(command, parsed);
    }

    private enum WrongMoveDirection implements MoveDirection {
        WRONG
    }
}
