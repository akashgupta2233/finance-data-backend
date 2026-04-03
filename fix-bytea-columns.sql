-- Fix BYTEA columns in PostgreSQL database
-- Run these commands in your Neon database console or via psql

-- Fix users table
ALTER TABLE users 
  ALTER COLUMN username TYPE VARCHAR(255) USING username::text,
  ALTER COLUMN email TYPE VARCHAR(255) USING email::text,
  ALTER COLUMN password TYPE VARCHAR(255) USING password::text;

-- Fix financial_records table
ALTER TABLE financial_records 
  ALTER COLUMN category TYPE VARCHAR(255) USING category::text,
  ALTER COLUMN notes TYPE TEXT USING notes::text;

-- Verify the changes
SELECT column_name, data_type, character_maximum_length 
FROM information_schema.columns 
WHERE table_name IN ('users', 'financial_records') 
  AND column_name IN ('username', 'email', 'password', 'category', 'notes')
ORDER BY table_name, column_name;
