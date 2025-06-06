import { Button, FormControl, InputLabel, MenuItem, TextField,Box, Select } from '@mui/material';
import React, { useState } from 'react'
import { addActivity } from '../services/api';




const ActivityForm = ({onActivityAdded}) => {

    const[activity,setActivity]=useState({
        type:"RUNNING",duration:'',caloriesBurned:'',
        additionalMetrics:{}

    })

    const handeSubmit=async(e)=>{
        e.preventDefault();
        try{
         await addActivity(activity);
          onActivityAdded();
          setActivity({type:"RUNNING",duration:'',caloriesBurned:''})
        }
        catch(error){
           console.error(error)
        }
    }

  return (
    <Box component="form" onSubmit={handeSubmit} sx={{ mb:4 }}>
      <FormControl fullWidth sx={{mb:2}}>
        <InputLabel>Actvity Type</InputLabel>
        <Select value={activity.type}
            onChange={(e)=>setActivity({...activity,type:e.target.value})}
        >
        <MenuItem value="RUNNING">Running</MenuItem>
        <MenuItem value="WALKING">Walking</MenuItem>
        <MenuItem value="CYCLING">Cycling</MenuItem>
        <MenuItem value="WEIGHT_TRAINING">Weight Training</MenuItem>
        <MenuItem value="YOGA">Yoga</MenuItem>
        <MenuItem value="SWIMMING">Swimming</MenuItem>
        <MenuItem value="HIIT">Hiit</MenuItem>
        <MenuItem value="CARDIO">Cardio</MenuItem>
        <MenuItem value="STRETCHNING">Stretching</MenuItem>
        <MenuItem value="OTHER">OTHER</MenuItem>
        

        </Select>
      </FormControl>

        <TextField fullWidth
                   label="Duration (Minutes)"
                   type='number'
                   sx={{mb:2}}
                   value={activity.duration}
                   onChange={(e)=>setActivity({...activity,duration:e.target.value})}

      />
       <TextField fullWidth
                   label="Calories Burned "
                   type='number'
                   sx={{mb:2}}
                   value={activity.caloriesBurned}
                   onChange={(e)=>setActivity({...activity,caloriesBurned:e.target.value})}
                   
      />

      <Button type='submit' variant='contained'>Add Activity</Button>
  </Box>
  )
}

export default ActivityForm
