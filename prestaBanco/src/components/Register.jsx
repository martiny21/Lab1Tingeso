import React, { useState } from "react";
import { TextField, Button, Container, Typography, Box, FormControl, FormHelperText } from "@mui/material";
import { format } from 'date-fns';
import userService from "../services/user.services";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const [name, setName] = useState("");
  const [rut, setRut] = useState("");
  const [birthDate, setBirthDate] = useState(null);
  const [salary, setSalary] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();


  const handleRegister = () => {
    // Check form
    if (!name || !rut || !birthDate || !salary) {
      setError("Todos los campos son obligatorios.");
      return;
    }

    // Check rut format
    if (!/^\d{8}-\d$/.test(rut)) {
      setError("Por favor, ingrese un RUT válido en el formato 12345678-9");
      return;
    }

    // Reset error message
    setError("");
    setBirthDate(format(birthDate, "yyyy-MM-dd"));

    
    const userData = {
      name,
      rut,
      salary: parseFloat(salary),
      birthDate,
    };

    userService.register(userData)
    .then((response) => {
      console.log("Usuario registrado", response.data);
      alert("Usuario registrado");
      
    })
    .catch((error) => {
      console.log(
        "Ha ocurrido un error al registrar el usuario",
        error
      );
    });
    
  };

  return (
    <Container maxWidth="xs">
      <Box
        display="flex"
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        
        <Typography variant="h4" component="h1" gutterBottom>
          Registro
        </Typography>
        <FormControl fullWidth sx={{ mt: 2}}>
              <TextField 
                id="nacimiento"
                label="Fecha de nacimiento"
                type="date"
                value={birthDate}
                variant="standard"
                margin="normal"
                onChange={(e) => setBirthDate((e.target.value))}
                InputLabelProps={{
                  shrink: true,
                }}
                sx={{backgroundColor: "rgba(255, 255, 255, 0.7)"}}
              />
        </FormControl>
        <TextField
          label="Nombre"
          variant="filled"
          fullWidth
          margin="normal"
          value={name}
          onChange={(e) => setName(e.target.value)}
          sx={{
            backgroundColor: "white", 
          }}
        />
        <TextField
          label="RUT"
          variant="filled"
          fullWidth
          margin="normal"
          value={rut}
          onChange={(e) => setRut(e.target.value)}
          error={!!error && !/^\d{8}-\d$/.test(rut)}
          helperText={!!error && !/^\d{8}-\d$/.test(rut) ? error : ""}
          sx={{
            backgroundColor: "white", 
          }}
        />
        <TextField
          label="Salario"
          variant="filled"
          fullWidth
          margin="normal"
          type="number"
          value={salary}
          onChange={(e) => setSalary(e.target.value)}
          sx={{
            backgroundColor: "white",
          }}
        />
        <FormHelperText error>{error}</FormHelperText>
        <Button
          variant="contained"
          color="primary"
          fullWidth
          onClick={handleRegister}
          sx={{ marginTop: "1rem"}}
        >
          Registrarse
        </Button>
      </Box>
    </Container>
  );
};

export default Register;
