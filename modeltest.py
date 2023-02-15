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
file = open("C:\\Users\\IMOE001\\Desktop\\output.txt","w")
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
    print(str(notes))
    return notes
    
    #with open('data/notes', 'wb') as filepath:
    #    pickle.dump(notes, filepath)
def prepare_sequences(notes,n_vocab):
    sequence_length = 100

      # get all pitch names
    pitchnames = sorted(set(item for item in notes))

      # create a dictionary to map pitches to integers
    note_to_int = dict((note, number) for number, note in enumerate(pitchnames))

    network_input = []
    network_output = []

      # create input sequences and the corresponding outputs
    for i in range(0, len(notes) - sequence_length, 1):
        sequence_in = notes[i:i + sequence_length]
        sequence_out = notes[i + sequence_length]
        network_input.append([note_to_int[char] for char in sequence_in])
        network_output.append(note_to_int[sequence_out])
    
    n_patterns = len(network_input)

      # reshape the input into a format compatible with LSTM layers
    network_input = numpy.reshape(network_input, (n_patterns, sequence_length, 1))
      # normalize input
    network_input = network_input / float(n_vocab)

    network_output = np_utils.to_categorical(network_output)

    return (network_input,network_output)
    
def create_network(network_input, n_vocab):
    #create the structure of the neural network
    model = Sequential()
    model.add(LSTM(
        512,
        input_shape=(network_input.shape[1], network_input.shape[2]),
        recurrent_dropout=0.3,
        return_sequences=True
    ))
    model.add(LSTM(512, return_sequences=True, recurrent_dropout=0.3,))
    model.add(Bidirectional(LSTM(512)))
    model.add(BatchNorm())
    model.add(Dropout(0.3))
    model.add(Dense(256))
    model.add(Activation('relu'))
    model.add(BatchNorm())
    model.add(Dropout(0.3))
    model.add(Dense(n_vocab))
    model.add(Activation('softmax'))
    model.compile(loss='categorical_crossentropy', optimizer='rmsprop', metrics=['accuracy'])

    return model    
    
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
        
        n_vocab = len(set(notes))

        network_input, network_output = prepare_sequences(notes, n_vocab)
        print(network_input)
        print(network_output)
        
        model = create_network(network_input,n_vocab)
        
        model.fit(network_input, network_output, epochs=10, batch_size=64)
       
       
except Error as e:
    file.write(str(e))
    print(str(e))
finally:
    file.close()

    if(conn.is_connected()):  
        cursor.close()
        conn.close()

