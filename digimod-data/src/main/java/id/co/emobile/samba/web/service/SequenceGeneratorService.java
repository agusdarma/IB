package id.co.emobile.samba.web.service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.data.IdSet;
import id.co.emobile.samba.web.entity.Counter;
import id.co.emobile.samba.web.mapper.CounterMapper;
import id.co.emobile.samba.web.sequence.SequenceStrategy;

//This service should not be annotated as Service, since it will be declared in spring context
public class SequenceGeneratorService {
	private static final Logger LOG = LoggerFactory.getLogger(SequenceGeneratorService.class);
	
	@Autowired
	protected AppsTimeService timeService;
	
	@Autowired
	protected CounterMapper counterMapper;
	
	protected SequenceStrategy sequenceStrategy;
	
	// appName: define the id of the counter used in system
	private String appName; // set once, will not be changed
	// range: define the range that system will cache the counter
	protected int range = 9999;  // set once, will not be changed
	// countMax: define the maximum number that is cached in the system
	private int countMax = 0;
	private AtomicInteger counter = new AtomicInteger(0);
	private Date period = null;
	
	private Counter cachedCounter;
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public void setSequenceStrategy(SequenceStrategy sequenceStrategy) {
		this.sequenceStrategy = sequenceStrategy;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getCountMax() {
		return countMax;
	}
	
	// these setter just for test purposes
	protected void setCounter(int counter) {
		this.counter = new AtomicInteger(counter);
	}
	protected void setCountMax(int countMax) {
		this.countMax = countMax;
	}
	protected void setPeriod(Date period) {
		this.period = period;
	}
	
	public void initService() throws SambaWebException {
//		LOG.debug("Test appName: " + appName);
		cachedCounter = counterMapper.findCounterByAppName(appName);
		LOG.debug("Test cachedCounter: " + cachedCounter);
		
//		period = timeService.getCurrentTime();
		int oldCounter = 0;
		if (cachedCounter == null) {
			//create the record
			cachedCounter = new Counter();
			cachedCounter.setAppName(appName);
			cachedCounter.setCounter(range);
			cachedCounter.setPeriod(timeService.getCurrentTime());
			counterMapper.createCounter(cachedCounter);
			LOG.debug("[{}] Created {}", appName, cachedCounter);
		} else if (sequenceStrategy.needResetSequence(cachedCounter.getPeriod())) {
			IdSet set = resetSequencePeriod();
			period = set.getPeriod();
			countMax = set.getMax();
			oldCounter = set.getMin() - 1;
		} else {
			IdSet set = updateMaxCount();
			countMax = set.getMax();
			oldCounter = set.getMin() - 1;
		}
		period = cachedCounter.getPeriod();
		countMax = cachedCounter.getCounter();
		counter.set(oldCounter);
		LOG.debug("[{}] SequenceGenerator has been started", appName);
	}
	
	public String getNextSequence() throws SambaWebException {
		try {
			Pair<Integer, Date> pair = getNextCounter();
			String seq = sequenceStrategy.formatSequence(pair.getLeft(), pair.getRight());
			LOG.debug("[" + appName + "] getNextSequence: " + seq);
			return seq;
		} catch (SambaWebException je) {
			throw je;
		} catch (Exception e) {
			LOG.warn("[" + appName + "] Error in getNextSequence", e);
			throw new SambaWebException(SambaWebException.NE_UNKNOWN_ERROR);
		}
	}
	
	/*
	 * Query to Database only when it reached max
	 * or when period is not today
	 * The id must not return 0
	 */
	private Pair<Integer, Date> getNextCounter() throws SambaWebException {
		if (sequenceStrategy.needResetSequence(period) ||
				(counter.get() + 1) > sequenceStrategy.getLimit()) {
			// when period is null, sequence strategy should need reset
			// when reach limit, then need to reset
			synchronized (this) {
				if (sequenceStrategy.needResetSequence(period) ||
						(counter.get() + 1) > sequenceStrategy.getLimit()) {
					LOG.debug("Need Reset Sequence for Period {}, Counter {}, Max {}", 
							period, counter.get(), sequenceStrategy.getLimit());
					IdSet set = resetSequencePeriod();
					countMax = set.getMax();
					period = set.getPeriod();
					counter.set(set.getMin());
					return new ImmutablePair<Integer, Date>(counter.get(), period);
				}  // end if inner needResetSequence
			}  // end synchronized
		}  // end if needResetSequence
		
		Date localPeriod = (Date) period.clone();
		int c = counter.incrementAndGet();
//		LOG.debug("[TESTING] Counter1: " + c + ", Max: " + countMax + ", Period: " + period);
		if (c > countMax) {
			// only handle countMax
			int localCountMax = countMax;
			synchronized (this) {
				if (!localPeriod.equals(period)) {
					//  period has been changed in previous synchronized block 
					c = counter.incrementAndGet();
					localPeriod = (Date) period.clone();
				} else if (localCountMax > countMax) {
					// countMax has been changed in previous synchronized block 
					c = counter.incrementAndGet();
				} else if (c > countMax) {
					IdSet set = updateMaxCount();
					countMax = set.getMax();
					if (localCountMax > countMax) {
						counter.set(set.getMin());
						c = counter.get();
					}
				} 
//				LOG.debug("[TESTING] CountMax: " + countMax);
			}  // end if synchronized
		}  // end if c > countMax
		
		return new ImmutablePair<Integer, Date>(c, localPeriod);
	}  // end getNexCounter
	
	/*
	 * reset counter if counter has not been defined in DB
	 * or if period needs to reset 
	 * this function should be called from synchronized block
	 * return IdSet
	 */
	private IdSet resetSequencePeriod() throws SambaWebException {
		Date currentTime = timeService.getCurrentTime();
//		if (cachedCounter == null) 
//			cachedCounter = counterMapper.findCounterByAppName(appName);
//
//		if (cachedCounter == null) {
//			//create the record
//			cachedCounter = new Counter();
//			cachedCounter.setAppName(appName);
//			cachedCounter.setCounter(range);
//			cachedCounter.setPeriod(currentTime);
//			counterMapper.createCounter(cachedCounter);
//			LOG.debug("[{}] Created {}", appName, cachedCounter);
//		} else {
			cachedCounter.setCounter(range);
			cachedCounter.setPeriod(currentTime);
			int updated = counterMapper.updateCounter(cachedCounter);
			cachedCounter.setOldCounter(cachedCounter.getCounter());
			LOG.debug("[{}] Updated {} {}", appName, updated, cachedCounter);
//		}
		IdSet idSet = new IdSet(1, range, currentTime);
		return idSet;
	}
	
	private IdSet updateMaxCount() throws SambaWebException {
		int min = cachedCounter.getCounter() + 1; 
		int max = cachedCounter.getCounter() + range;
		max = Math.min(max, sequenceStrategy.getLimit());
		if (min > max) { 
			//reset
			min = 1;
			max = range;
		}
		cachedCounter.setCounter(max);
		
		int bulletProof = 0;
		int updated = counterMapper.updateCounter(cachedCounter);
		while (updated != 1 && bulletProof++ < 5) {
			cachedCounter = counterMapper.findCounterByAppName(appName);
			min = cachedCounter.getCounter() + 1; 
			max = cachedCounter.getCounter() + range;
			max = Math.min(max, sequenceStrategy.getLimit());
			if (min > max) { 
				//reset
				min = 1;
				max = range;
			}
			cachedCounter.setCounter(max);
			updated = counterMapper.updateCounter(cachedCounter);
		}
		if (updated == 0) {
			String msg = "Unable to updateMaxCount for Apps " + appName;
			LOG.warn(msg);
			throw new SambaWebException(SambaWebException.NE_UNKNOWN_ERROR);
		}
		cachedCounter.setOldCounter(cachedCounter.getCounter());
//		LOG.debug("[TESTING] UpdateMaxCount: " + cachedCounter);
		IdSet idSet = new IdSet(min,max,cachedCounter.getPeriod());
		return idSet;
	}
	
	public int getCurrentCount() {
		return counter.get();
	}
}


