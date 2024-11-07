import React, { useState } from "react";
import { TextField, Button, Container, Typography, Box, FormHelperText } from "@mui/material";
import { useNavigate } from "react-router-dom"; // Asegúrate de importar useNavigate

const Home = () => {
  const [rut, setRut] = useState(""); // Estado inicial vacío para RUT
  const [error, setError] = useState(""); // Estado para manejar el mensaje de error
  const navigate = useNavigate(); // Inicializa useNavigate

  const handleRutChange = (e) => {
    const value = e.target.value;

    // Verifica si el nuevo valor es un RUT válido hasta el momento
    if (/^\d{0,8}-?\d?$/.test(value)) {
      setRut(value);
      setError(""); // Resetea el error al cambiar el RUT
    }
  };

  const handleLoginAsExecutive = () => {
    navigate('Executive');
  };

  const handleLoginAsClient = () => {
    // Checking rut format 12345678-9
    if (/^\d{8}-\d$/.test(rut)) {
      console.log("Iniciar sesión como cliente con RUT:", rut);
      sessionStorage.setItem("userRut", rut);
      navigate('/Client');
    } else {
      setError("Por favor, ingrese un RUT válido en el formato 12345678-9.");
    }
  };

  const handleRegister = () => {
    navigate('/Register');
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
          Iniciar Sesión
        </Typography>
        <TextField
          label="RUT"
          variant="filled"
          fullWidth
          margin="normal"
          value={rut}
          onChange={handleRutChange}
          sx={{
            backgroundColor: "white",
          }}
          error={!!error}
        />
        <FormHelperText error>{error}</FormHelperText> 
        <Box display="flex" width="100%" marginBottom="1rem">
            <Button
                variant="contained"
                color="primary"
                fullWidth
                onClick={handleLoginAsClient}
                sx={{ marginLeft: "0.5rem" }} // Space between buttons
            >
                Entrar como Cliente
            </Button>
            <Button
                variant="contained"
                color="primary"
                fullWidth
                onClick={handleLoginAsExecutive}
                sx={{ marginRight: "0.5rem" }} 
            >
                Entrar como Ejecutivo
            </Button>
        </Box>
        <Button
          variant="outlined"
          color="secondary"
          fullWidth
          onClick={handleRegister}
        >
          Registrarse
        </Button>
      </Box>
    </Container>
  );
};

export default Home;
