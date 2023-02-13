import sys
import mysql.connector
from mysql.connector import Error
import glob
import numpy
from music21 import converter, instrument, note, chord
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import Dropout
from keras.layers import LSTM
from keras.layers import Activation
from keras.layers import BatchNormalization as BatchNorm
from keras.layers import Bidirectional
from keras.utils import np_utils
from keras.callbacks import ModelCheckpoint

# Recieves the id from java code
id = sys.argv[1]

# WHEN WRITING TO A FILE MUST SURROUND WITH STR()!!!!!
file = open("C:\\Users\\WIN10\\Desktop\\output.txt","w")
file.write(id)

def extract_notes(midiFiles):
    notes=[]
    #Iterate over every file in list
    #Note: every list recieved here is a file list that consists of only one genre
    for file in midiFiles:
        #parse bytes from each file into a midi format using 
        midi = converter.parse(file[0])
        
        notes_to_parse = None

        try: # file has instrument parts(different tracks)
            s2 = instrument.partitionByInstrument(midi)
            notes_to_parse = s2.parts[0].recurse() 
        except: # file has notes in a flat structure
            notes_to_parse = midi.flat.notes
        
        #from notes to parse list of notes & chords
        for element in notes_to_parse:
            #for note: append note to list
            if isinstance(element, note.Note):
                notes.append(str(element.pitch))
            #for chord: iterate over all the chord notes and add them to the list
            elif isinstance(element, chord.Chord):
                notes.append('.'.join(str(n) for n in element.normalOrder))
                
    return notes
    
    #with open('data/notes', 'wb') as filepath:
    #    pickle.dump(notes, filepath)

    
try:
    # Establish a connection to the database
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="admin",
        database="mididataset",
    )
   
    if(conn.is_connected()):
        
        # Create a cursor object
        cursor = conn.cursor(buffered = True)
      
        cursor.execute("select database();")
     
        # Execute a SQL query that returns all files from a specified genre
        cursor.execute("SELECT file FROM midi_file where genreid = "+str(id) +";")
        

        # Fetch the results of the query
        midiFiles = cursor.fetchall()
        
        #extract from all midifiles an array of encoded notes.
        notes = extract_notes(midiFiles)
        file.write(str(notes))
       
except Error as e:
    file.write(str(e))
finally:
    file.close()

    if(conn.is_connected()):  
        cursor.close()
        conn.close()

