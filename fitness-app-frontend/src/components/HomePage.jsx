import React from 'react';
import { Box, Card, CardContent, Typography, Button } from '@mui/material';
import { useContext } from 'react';
import { AuthContext } from 'react-oauth2-code-pkce';

const HomePage = () => {
  const { logIn } = useContext(AuthContext);

  return (
    <Box
      sx={{
        height: '100vh',
        backgroundColor: '#f0f4f8',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <Card
        sx={{
          width: 400,
          textAlign: 'center',
          p: 3,
          boxShadow: 4,
          borderRadius: 4,
          backgroundColor: '#ffffff',
        }}
      >
        <CardContent>
          <Typography variant="h4" sx={{ fontWeight: 'bold', color: '#1976d2', mb: 1 }}>
            Fitness Tracker App
          </Typography>
          <Typography variant="subtitle1" sx={{ mb: 3, color: '#555' }}>
            Please login to continue
          </Typography>
          <Button
            variant="contained"
            sx={{ backgroundColor: '#dc004e', '&:hover': { backgroundColor: '#b0003a' } }}
            fullWidth
            onClick={()=>{
                logIn();
              }}
          >
            Login
          </Button>
        </CardContent>
      </Card>
    </Box>
  );
};

export default HomePage;
