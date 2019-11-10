CREATE OR REPLACE FUNCTION trigger_set_updated_time()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_time = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;