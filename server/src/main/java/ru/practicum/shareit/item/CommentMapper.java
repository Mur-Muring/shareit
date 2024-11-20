package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class CommentMapper {

    public static CommentDto toDtoComment(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItem().getId())
                .authorName(comment.getAuthor().getName())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, Item item, User author) {
        return Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(author)
                .build();
    }
}
