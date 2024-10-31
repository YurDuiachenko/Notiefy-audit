CREATE TABLE IF NOT EXISTS played_song
(
    name String,
    genre String,
    musicianNickName String,
    timePlayed timestamp
) ENGINE = Log()