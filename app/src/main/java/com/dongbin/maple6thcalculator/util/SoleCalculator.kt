package com.dongbin.maple6thcalculator.util


data class ReqSole(
    val erda: Int,
    val erdaPiece: Int,
)

val originSoleErdaReq = listOf(
    0,
    1,
    1,
    1,
    2,
    2,
    2,
    3,
    3,
    10,
    3,
    3,
    4,
    4,
    4,
    4,
    4,
    4,
    5,
    15,
    5,
    5,
    5,
    5,
    5,
    6,
    6,
    6,
    7,
    20
)
val originSoleErdaPieceReq = listOf(
    0,
    30,
    35,
    40,
    45,
    50,
    55,
    60,
    65,
    200,
    80,
    90,
    100,
    110,
    120,
    130,
    140,
    150,
    160,
    350,
    170,
    180,
    190,
    200,
    210,
    220,
    230,
    240,
    250,
    500
)
val masterySoleErdaReq = listOf(
    3,
    1,
    1,
    1,
    1,
    1,
    1,
    2,
    2,
    5,
    2,
    2,
    2,
    2,
    2,
    2,
    2,
    2,
    3,
    8,
    3,
    3,
    3,
    3,
    3,
    3,
    3,
    3,
    4,
    10
)
val masterySoleErdaPieceReq = listOf(
    50,
    5,
    18,
    20,
    23,
    25,
    28,
    30,
    33,
    100,
    40,
    45,
    50,
    55,
    60,
    65,
    70,
    75,
    80,
    175,
    85,
    90,
    95,
    100,
    105,
    110,
    115,
    120,
    125,
    250
)
val enhanceSoleErdaReq = listOf(
    4,
    1,
    1,
    1,
    2,
    2,
    2,
    3,
    3,
    8,
    3,
    3,
    3,
    3,
    3,
    3,
    3,
    3,
    4,
    12,
    4,
    4,
    4,
    4,
    4,
    5,
    5,
    5,
    6,
    15
)
val enhanceSoleErdaPieceReq = listOf(
    75,
    23,
    27,
    30,
    34,
    38,
    42,
    45,
    49,
    150,
    60,
    68,
    75,
    83,
    90,
    98,
    105,
    113,
    120,
    263,
    128,
    135,
    143,
    150,
    158,
    165,
    173,
    180,
    188,
    375
)

fun calculate(
    nowOriginLevel: Int, goalOriginLevel: Int, nowMasteryLevel: Int, goalMasteryLevel: Int,
    nowEnhanceFirstLevel: Int, goalEnhanceFirstLevel: Int,
    nowEnhanceSecondLevel: Int, goalEnhanceSecondLevel: Int,
    nowEnhanceThirdLevel: Int, goalEnhanceThirdLevel: Int,
    nowEnhanceFourthLevel: Int, goalEnhanceFourthLevel: Int,
): ReqSole {

    var soleErda: Int = 0
    var soleErdaPiece: Int = 0

    if (nowOriginLevel <= goalOriginLevel) {
        soleErda += originSoleErdaReq.subList(nowOriginLevel - 1, goalOriginLevel - 1).sum()
        soleErdaPiece += originSoleErdaPieceReq.subList(nowOriginLevel - 1, goalOriginLevel - 1).sum()
    }
    if (nowMasteryLevel <= goalMasteryLevel) {
        soleErda += masterySoleErdaReq.subList(nowMasteryLevel - 1, goalMasteryLevel - 1).sum()
        soleErdaPiece += masterySoleErdaPieceReq.subList(nowMasteryLevel - 1, goalMasteryLevel - 1).sum()
    }
    if (nowEnhanceFirstLevel <= goalEnhanceFirstLevel) {
        soleErda += enhanceSoleErdaReq.subList(nowEnhanceFirstLevel - 1, goalEnhanceFirstLevel - 1).sum()
        soleErdaPiece += enhanceSoleErdaPieceReq.subList(nowEnhanceFirstLevel - 1, goalEnhanceFirstLevel - 1).sum()
    }
    if (nowEnhanceSecondLevel <= goalEnhanceSecondLevel) {
        soleErda += enhanceSoleErdaReq.subList(nowEnhanceSecondLevel - 1, goalEnhanceSecondLevel - 1).sum()
        soleErdaPiece += enhanceSoleErdaPieceReq.subList(nowEnhanceSecondLevel - 1, goalEnhanceSecondLevel - 1).sum()
    }
    if (nowEnhanceThirdLevel <= goalEnhanceThirdLevel) {
        soleErda += enhanceSoleErdaReq.subList(nowEnhanceThirdLevel - 1, goalEnhanceThirdLevel - 1).sum()
        soleErdaPiece += enhanceSoleErdaPieceReq.subList(nowEnhanceThirdLevel - 1, goalEnhanceThirdLevel - 1).sum()
    }
    if (nowEnhanceFourthLevel <= goalEnhanceFourthLevel) {
        soleErda += enhanceSoleErdaReq.subList(nowEnhanceFourthLevel - 1, goalEnhanceFourthLevel - 1).sum()
        soleErdaPiece += enhanceSoleErdaPieceReq.subList(nowEnhanceFourthLevel - 1, goalEnhanceFourthLevel - 1).sum()
    }

    return ReqSole(soleErda, soleErdaPiece)
}