-- 既存のトレーニング記録を削除（ユーザー紐付けがないため）
DELETE FROM training_records;

-- training_recordsテーブルにuser_idカラムを追加
ALTER TABLE training_records
ADD COLUMN user_id BIGINT NOT NULL AFTER id;

-- 外部キー制約を追加
ALTER TABLE training_records
ADD CONSTRAINT fk_training_records_user
FOREIGN KEY (user_id) REFERENCES users(id);

-- インデックスを追加
CREATE INDEX idx_training_records_user_id ON training_records(user_id);
CREATE INDEX idx_training_records_user_date ON training_records(user_id, training_date);
