import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { getActivityDetail } from '../services/api';
import {
  Box,
  Card,
  CardContent,
  Divider,
  Typography,
  List,
  ListItem,
  ListItemText,
  Chip
} from '@mui/material';

const labelStyle = { fontWeight: 'bold', color: '#3f51b5' };
const listBulletStyle = { color: '#4caf50', fontWeight: 500 };

const ActivityDetail = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);

  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        const response = await getActivityDetail(id);
        setActivity(response.data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchActivityDetail();
  }, [id]);

  if (!activity) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto', p: 3 }}>
      {/* Activity Overview */}
      <Card sx={{ mb: 3, backgroundColor: '#f5f5f5' }}>
        <CardContent>
          <Typography variant="h4" gutterBottom sx={{ color: '#1976d2', fontWeight: 'bold' }}>
            Activity Overview
          </Typography>
          <Typography><span style={labelStyle}>Type:</span> {activity.activityType}</Typography>
          <Typography><span style={labelStyle}>Duration:</span> {activity.duration} minutes</Typography>
          <Typography><span style={labelStyle}>Calories Burned:</span> {activity.caloriesBurned}</Typography>
          <Typography><span style={labelStyle}>Date:</span> {new Date(activity.createdAt).toLocaleString()}</Typography>
        </CardContent>
      </Card>

      {/* AI Recommendation */}
      <Card sx={{ backgroundColor: 'white' }}>
        <CardContent>
          <Typography variant="h5" gutterBottom sx={{ fontWeight: 'bold', color: '#1565c0' }}>
            AI Recommendation
          </Typography>

          <Typography variant="h6" gutterBottom sx={{ color: '#00796b', fontWeight: 'bold' }}>
            Analysis
          </Typography>
          <Typography paragraph sx={{ whiteSpace: 'pre-line', mb: 2 }}>
            {activity.recommendation}
          </Typography>

          <Divider sx={{ my: 2 }} />
          <Typography variant="h6" gutterBottom sx={{ color: '#6a1b9a', fontWeight: 'bold' }}>
            Improvements
          </Typography>
          <List>
            {activity?.improvements?.map((item, index) => (
              <ListItem key={index} sx={{ pl: 1 }}>
                <ListItemText primary={<span style={listBulletStyle}>• {item}</span>} />
              </ListItem>
            ))}
          </List>

          <Divider sx={{ my: 2 }} />
          <Typography variant="h6" gutterBottom sx={{ color: '#ef6c00', fontWeight: 'bold' }}>
            Suggestions
          </Typography>
          <List>
            {activity?.suggestions?.map((item, index) => (
              <ListItem key={index} sx={{ pl: 1 }}>
                <ListItemText primary={<span style={listBulletStyle}>• {item}</span>} />
              </ListItem>
            ))}
          </List>

          <Divider sx={{ my: 2 }} />
          <Typography variant="h6" gutterBottom sx={{ color: '#c62828', fontWeight: 'bold' }}>
            Safety Guidelines
          </Typography>
          <List>
            {activity?.safety?.map((item, index) => (
              <ListItem key={index} sx={{ pl: 1 }}>
                <ListItemText primary={<span style={listBulletStyle}>• {item}</span>} />
              </ListItem>
            ))}
          </List>
        </CardContent>
      </Card>
    </Box>
  );
};

export default ActivityDetail;
