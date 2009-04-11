package de.lessvoid.nifty.sound.spi;

/**
 * A interface to a piece of sound. Ready to be played.
 * @author void
 */
public interface SoundHandle {

  /**
   * Play the sound.
   */
  void play();

  /**
   * Stop the sound.
   */
  void stop();

  /**
   * Change volume of the sound while it is playing.
   * @param volume new value in range 0 to 1
   */
  void setVolume(float volume);
}
