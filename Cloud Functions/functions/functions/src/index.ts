import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

// Initialize Firebase Admin SDK
admin.initializeApp();
const db = admin.database();

// Function to check if a number is prime
/**
 * Yuh
 * @param {number} num
 * @return {boolean}
 */
function isPrime(num: number): boolean {
  if (num <= 1) return false; // Numbers less than or equal to 1 are not prime
  for (let i = 2; i <= Math.sqrt(num); i++) {
    if (num % i === 0) {
      return false; // Not a prime number if divisible by other numbers
    }
  }
  return true;
}

// Cloud Function to process numbers from the database
export const processNumbersFromDatabase = functions.https.
  onRequest(async (req, res) => {
    try {
    // Read the "numbers" node from Realtime Database
      const snapshot = await db.ref("numbers").once("value");

      // Extract numbers from the snapshot
      const numbers = snapshot.val();
      if (!numbers) {
        res.status(400).send({
          success: false,
          message: "No numbers found in the database.",
          result: null,
        });
        return; // End the function after sending the response
      }

      // Convert the numbers to an array (if stored as an object or array in DB)
      const numberArray = Object.values(numbers) as number[];
      console.log("Numbers retrieved from database:", numberArray);

      // Sum the numbers and check if the sum is prime
      const sum = numberArray.reduce((acc, curr) => acc + curr, 0);
      const isSumPrime = isPrime(sum);

      // Return the boolean result and other information
      res.status(200).send({
        success: true,
        isPrime: isSumPrime,
        sum,
      });
    } catch (error) {
      console.error("Error processing numbers:", error);
      res.status(500).send({
        success: false,
        message: "An error occurred while processing numbers.",
        result: null,
      });
    }
  });

export const updateGreatestOccurrence = functions.database
  .onValueWritten("/numbers", async (event) => {
    // Get all data under the "numbers" node
    const numbersSnapshot = await admin.database().ref("/numbers")
      .once("value");
    const numbers = numbersSnapshot.val();

    // If no data found, exit the function
    if (!numbers) {
      console.log("No numbers found in the database.");
      return null;
    }

    const numberFrequency: { [key: string]: number } = {};

    // Count the frequency of each number
    for (const key in numbers) {
      if (Object.prototype.hasOwnProperty.call(numbers, key)) {
        const number = numbers[key];
        numberFrequency[number] = (numberFrequency[number] || 0) + 1;
      }
    }

    // Find the number with the highest frequency
    let mostFrequentNumber = null;
    let highestFrequency = 0;
    for (const num in numberFrequency) {
      if (numberFrequency[num] > highestFrequency) {
        mostFrequentNumber = num;
        highestFrequency = numberFrequency[num];
      }
    }

    // Write the result to the "GreatestOccurence" node
    await admin.database().ref("/GreatestOccurence").set({
      number: mostFrequentNumber,
      frequency: highestFrequency,
    });

    console.log(`Most frequent number: ${mostFrequentNumber} 
      with frequency: ${highestFrequency}`);

    return null; // End the function
  });
