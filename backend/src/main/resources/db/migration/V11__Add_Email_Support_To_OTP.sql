-- Add email support to OTP requests
ALTER TABLE otp_requests 
    ADD COLUMN email VARCHAR(100),
    ALTER COLUMN phone_number DROP NOT NULL;

-- Create index for email lookups
CREATE INDEX IF NOT EXISTS idx_otp_requests_email ON otp_requests(email);

-- Add constraint to ensure either phone or email is provided
ALTER TABLE otp_requests 
    ADD CONSTRAINT chk_otp_phone_or_email 
    CHECK (phone_number IS NOT NULL OR email IS NOT NULL);
