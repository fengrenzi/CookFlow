import request from '@/utils/request';

export function getRankingLists() {
  return request({
    url: '/explore/rankings',
    method: 'get'
  });
}

export function getHistoricalEvents() {
  return request({
    url: '/explore/historical-events',
    method: 'get'
  });
}