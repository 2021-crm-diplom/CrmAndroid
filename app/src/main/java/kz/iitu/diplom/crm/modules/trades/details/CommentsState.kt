package kz.iitu.diplom.crm.modules.trades.details

import kz.iitu.diplom.crm.modules.trades.Status
import kz.iitu.diplom.crm.modules.trades.models.Comment

class CommentsState(
    val status: Status,
    val comments: List<Comment> = listOf()
)