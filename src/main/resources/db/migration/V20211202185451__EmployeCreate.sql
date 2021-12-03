CREATE TABLE IF NOT EXISTS employee (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR (20) NOT NULL,
    email VARCHAR (30) NOT NULL,
    age INTEGER NOT NULL,
    contract_id UUID NOT NULL,
    state_id INTEGER REFERENCES employee_state(id)
);